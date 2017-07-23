package com.dashu.buyer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dashu.buyer.admin.AdminMainActivity;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.buyer.UserMainActivity;
import com.dashu.buyer.seller.SellerMainActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity implements OnClickListener {
    String account;
    String password;
    EditText accountEditText;
    EditText passwordEditText;
    int roleType;
    RadioButton managerRadiobutton;
    RadioButton userRadiobutton;
    RadioButton shopperRadiobutton;
    private String curAccount;
    Button loginButton;
    private SharedPreferences sp;
    private String pwd = null;
    private CustomProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        context = this;
        Log.w(TAG, "onCreate");

        pwd = StaticUtil.checkExistUser(this);
        if (pwd != null) {
            Log.w(TAG, "auto login");
            testLogin(StaticUtil.currentUser.getUsername(), pwd, roleType);
        }
        Log.w(TAG, "normal login");
        initView();
    }

    private void initView() {
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        findViewById(R.id.login_register_btn).setOnClickListener(this);
        findViewById(R.id.login_pwdseen).setOnClickListener(this);
        findViewById(R.id.login_forget_btn).setOnClickListener(this);
        accountEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        userRadiobutton = (RadioButton) findViewById(R.id.login_radio_user);
        managerRadiobutton = (RadioButton) findViewById(R.id.login_radio_manager);
        shopperRadiobutton = (RadioButton) findViewById(R.id.login_radio_shoper);
        accountEditText.setText("");
        passwordEditText.setText("");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_button:
                //loginButton.setEnabled(false);
                doLogin();
                break;
            case R.id.login_register_btn:
                doRegister();
                break;
            case R.id.login_pwdseen:
                pwdClick();
                break;
            case R.id.login_forget_btn:
                break;
            default:
                break;
        }
    }

    boolean isHidden = true;

    public void pwdClick() {
        if (!isHidden) {//设置EditText文本为可见的
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {//设置EditText文本为隐藏的
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        isHidden = !isHidden;
        passwordEditText.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = passwordEditText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }

    /**
     * 登陆用户
     */
    private void testLogin(final String username, final String password, final int roleType) {
        Log.w("TAG", "roleType = " + roleType);
        progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.show();
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoleType(roleType);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                StaticUtil.currentUser = BmobUser.getCurrentUser(LoginActivity.this, User.class);

                if (pwd == null) {
                    toast("登陆成功");
                    StaticUtil.setCurrentUser(getApplicationContext(), StaticUtil.currentUser, password);
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                login();
            }

            @Override
            public void onFailure(int i, String s) {
                pwd = null;
                toast("用户名或密码错误！");
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });

    }

    private void saveCurrentLoginState() {
        Log.w(TAG, "saveCurrentLoginState");
        SharedPreferences userSettings = getSharedPreferences("cur_user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();

        editor.putString("name", accountEditText.getText().toString());
        editor.putString("pwd", passwordEditText.getText().toString());
    }

    private void jumpTo(Class targetActivity, String username) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        intent.putExtra("username", username);
        intent.putExtra("role", roleType);
        intent.putExtra("obj_id", StaticUtil.currentUser.getObjectId());
        startActivity(intent);
    }

    private void login() {
        if (StaticUtil.currentUser.getRoleType() == StaticUtil.ROLE_MANAGER) {
            jumpTo(AdminMainActivity.class, StaticUtil.currentUser.getUsername());
            finish();
        } else if (StaticUtil.currentUser.getRoleType() == StaticUtil.ROLE_USER) {
            jumpTo(UserMainActivity.class, StaticUtil.currentUser.getUsername());
            finish();
        } else {
            jumpTo(SellerMainActivity.class, StaticUtil.currentUser.getUsername());
            finish();
        }
    }

    void doLogin() {
        if (managerRadiobutton.isChecked()) {
            roleType = StaticUtil.ROLE_MANAGER;
        } else if (userRadiobutton.isChecked()) {
            roleType = StaticUtil.ROLE_USER;
        } else if (shopperRadiobutton.isChecked()) {
            roleType = StaticUtil.ROLE_SHOPPER;
        }

        account = accountEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        if (account.equals("") || password.equals("")) {
            ToastUtils.showTip(LoginActivity.this, "用户名或者密码不能为空！");
            return;
        }

        testLogin(account, password, roleType);
    }

    public void doRegister() {
        Intent mIntent = new Intent();
        mIntent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(mIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 100) {
                accountEditText.setText(data.getStringExtra("cur_account"));
                if (data.getIntExtra("role", 0) == 0) {
                    userRadiobutton.setChecked(true);
                } else if (data.getIntExtra("role", 0) == 1) {
                    managerRadiobutton.setChecked(true);
                } else {
                    shopperRadiobutton.setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.w(TAG, "on key back, do nothing");
        }
        return super.onKeyDown(keyCode, event);
    }
}
