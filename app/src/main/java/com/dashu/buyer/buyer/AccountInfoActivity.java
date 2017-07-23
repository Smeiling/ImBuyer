package com.dashu.buyer.buyer;

import android.app.Activity;
import android.content.Intent;
import android.sax.TextElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dashu.buyer.LoginActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.bean.UserInfo;
import com.dashu.buyer.util.StaticUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AccountInfoActivity extends Activity implements TextWatcher {

    private TextView tvSave;
    private EditText tvAccount;
    private EditText tvNick;
    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etIdcard;
    private EditText etSex;
    private Button btnLogout;
    private TextView tvAddress;

    private User curUser;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        if (null != getIntent().getSerializableExtra("user")) {
            curUser = (User) getIntent().getSerializableExtra("user");
        } else {
            curUser = StaticUtil.currentUser;
        }
        initView();
        queryForUser();
    }

    private void queryForUser() {
        BmobQuery<UserInfo> userInfo = new BmobQuery<>();
        userInfo.addWhereEqualTo("userObjId", curUser.getObjectId());
        userInfo.findObjects(getApplicationContext(), new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                fillContent(list.get(0));
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void fillContent(UserInfo userInfo) {
        this.userInfo = userInfo;
        tvAccount.setText(userInfo.getUsername());
        tvNick.setText(userInfo.getNick());
        etIdcard.setText(userInfo.getIdCard() == null ? "" : userInfo.getIdCard());
        etSex.setText(userInfo.getSex() == null ? "" : userInfo.getSex());
        etName.setText(userInfo.getName() == null ? "" : userInfo.getName());
        etPhone.setText(userInfo.getPhone());
        etAddress.setText(userInfo.getAddress() == null ? "" : userInfo.getAddress());
        if (userInfo.getRoleType() == StaticUtil.ROLE_USER) {
            tvAccount.setEnabled(false);
            tvNick.setEnabled(false);
        }
        if (userInfo.getRoleType() == StaticUtil.ROLE_SHOPPER) {
            tvAccount.setEnabled(false);
            tvNick.setEnabled(false);
            etName.setVisibility(View.INVISIBLE);
            tvAddress.setText("发货地址");
            tvSave.setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_r).setVisibility(View.GONE);
        }
        if (StaticUtil.currentUser.getRoleType() != userInfo.getRoleType()) {
            tvAccount.setEnabled(false);
            tvNick.setEnabled(false);
            etName.setEnabled(false);
            etPhone.setEnabled(false);
            etAddress.setEnabled(false);
            btnLogout.setVisibility(View.INVISIBLE);
            tvSave.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        etIdcard = (EditText) findViewById(R.id.et_idcard);
        etSex = (EditText) findViewById(R.id.et_sex);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticUtil.clearSP(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tvSave = (TextView) findViewById(R.id.tv_commit);
        tvSave.setVisibility(View.INVISIBLE);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
                tvSave.setVisibility(View.INVISIBLE);
            }
        });
        tvAccount = (EditText) findViewById(R.id.tv_account);
        tvNick = (EditText) findViewById(R.id.tv_nick);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        etName.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etAddress.addTextChangedListener(this);
        etSex.addTextChangedListener(this);
        etIdcard.addTextChangedListener(this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUserInfo() {
        User user = curUser;
        UserInfo info = userInfo;
        info.setNick(tvNick.getText().toString());
        info.setName(etName.getText().toString().isEmpty() ? "" : etName.getText().toString());
        info.setAddress(etAddress.getText().toString().isEmpty() ? "" : etAddress.getText().toString());
        info.setPhone(etPhone.getText().toString().isEmpty() ? "" : etPhone.getText().toString());
        info.setIdCard(etIdcard.getText().toString().isEmpty() ? "" : etIdcard.getText().toString());
        info.setSex(etSex.getText().toString().isEmpty() ? "" : etSex.getText().toString());
        user.setNick(tvNick.getText().toString());
        user.setReceiver(etName.getText().toString().isEmpty() ? "" : etName.getText().toString());
        user.setMobilePhoneNumber(etPhone.getText().toString().isEmpty() ? "" : etPhone.getText().toString());
        user.setAddress(etAddress.getText().toString().isEmpty() ? "" : etAddress.getText().toString());
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), " >.< 更新失败，貌似出了点问题！", Toast.LENGTH_SHORT).show();
                Log.w(StaticUtil.TAG, "error:" + i + s);
            }
        });
        info.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.w(StaticUtil.TAG, "update success");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.w(StaticUtil.TAG, "error:" + i + s);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etName.getText().toString().isEmpty() && etPhone.getText().toString().isEmpty() && etAddress.getText().toString().isEmpty()) {
            tvSave.setVisibility(View.INVISIBLE);
        } else {
            tvSave.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
