package com.dashu.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText cardEditText;
    private EditText passwordEditText;
    private EditText double_passEditText;
    private Button registerButton;
    private Button cancelButton;
    private Spinner spinner;
    private TextInputLayout icard_layout;
    private EditText etNickname;
    private User user;
    private int roleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        nameEditText = (EditText) findViewById(R.id.editText_input_name);
        cardEditText = (EditText) findViewById(R.id.editText_input_card);
        passwordEditText = (EditText) findViewById(R.id.editText_input_password);
        double_passEditText = (EditText) findViewById(R.id.editText_input_double_pass);
        icard_layout = (TextInputLayout) findViewById(R.id.icard_layout);
        registerButton = (Button) findViewById(R.id.button_register_ok);
        cancelButton = (Button) findViewById(R.id.button_register_cancel);
        etNickname = (EditText) findViewById(R.id.editText_nickname);
        registerButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner_role);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    roleType = StaticUtil.ROLE_USER;
                    icard_layout.setVisibility(View.GONE);
                } else {
                    roleType = StaticUtil.ROLE_SHOPPER;
                    icard_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register_ok:
                valueCheck();
                break;
            case R.id.button_register_cancel:
                setResult(100, null);
                finish();
                break;

            default:
                break;
        }
    }

    void valueCheck() {
        String account = nameEditText.getText().toString().trim();
        String card = cardEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String repass = double_passEditText.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        if (account.equals("") || password.equals("") || repass.equals("")) {
            Toast.makeText(getApplicationContext(), "用户名或者密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nickname.equals("")) {
            Toast.makeText(getApplicationContext(), "昵称不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (account.length() != 11) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), "请输入8位数以上的密码以确保账号安全！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleType == StaticUtil.ROLE_SHOPPER) {
            if (card == null || card.isEmpty() || card.length() != 18) {
                Toast.makeText(getApplicationContext(), "请输入正确的身份证号码！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!password.equals(repass)) {
            Toast.makeText(getApplicationContext(), "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        user = new User();
        user.setUsername(account);
        user.setPassword(password);
        if (card != null && !card.isEmpty()) {
            user.setCard(card);
        }
        user.setRoleType(roleType);
        if (nickname != null && !nickname.isEmpty()) {
            user.setNick(nickname);
        } else {
            user.setNick(account.substring(1, 8));
        }
        user.setMobilePhoneNumber(account);

        validateBeforeRegister(user);
    }

    private void validateBeforeRegister(User user) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", user.getUsername());
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list.size() > 0) {
                    Toast.makeText(getApplicationContext(), "该号码已注册过，请直接登录！", Toast.LENGTH_SHORT).show();
                } else {
                    register();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.w(TAG, "query error " + i + s);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void register() {
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast(" ^_^ 注册成功，请登录！");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("cur_account", nameEditText.getText().toString());
                intent.putExtra("cur_role",roleType);
                intent.putExtra("cur_object_id", user.getObjectId());
                setResult(100, intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                if (code == 301) {
                    toast("请输入正确的手机号码！");
                } else {
                    toast(" >.< 注册失败，请尝试重新注册！");
                }
                Log.w(TAG, "register error " + code + msg);
            }
        });
    }

}
