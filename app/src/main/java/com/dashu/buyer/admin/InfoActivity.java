package com.dashu.buyer.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

public class InfoActivity extends Activity {

    private TextView tvSave;
    private TextView tvEdit;
    private TextView tvAddress;
    private EditText tvAccount;
    private EditText tvNick;
    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etIdcard;
    private EditText etSex;
    private Button removeAccount;

    private UserInfo curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        curUser = (UserInfo) getIntent().getSerializableExtra("user");
        initView();
        if (curUser != null) {
            fillContent();
        }
    }


    private void fillContent() {
        tvAccount.setText(curUser.getUsername());
        tvNick.setText(curUser.getNick());
        etName.setText(curUser.getName() == null ? "" : curUser.getName());
        etPhone.setText(curUser.getPhone());
        etAddress.setText(curUser.getAddress() == null ? "" : curUser.getAddress());
        etIdcard.setText(curUser.getIdCard() == null ? "" : curUser.getIdCard());
        etSex.setText(curUser.getSex() == null ? "" : curUser.getSex());
        tvAccount.setEnabled(false);
        if (curUser.getRoleType() == StaticUtil.ROLE_SHOPPER) {
            findViewById(R.id.tv_r).setVisibility(View.GONE);
            tvAddress.setText("发货地址");
        }
        switchEditMode(false);
    }

    private void initView() {
        tvAddress = (TextView) findViewById(R.id.tv_address);
        etIdcard = (EditText) findViewById(R.id.et_idcard);
        etSex = (EditText) findViewById(R.id.et_sex);
        removeAccount = (Button) findViewById(R.id.btn_logout);
        removeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curUser.delete(getApplicationContext(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.w(StaticUtil.TAG, "delete success");
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.w(StaticUtil.TAG, "delete fail" + i + s);
                    }
                });
            }
        });
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvEdit.setVisibility(View.VISIBLE);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEditMode(true);
                tvEdit.setVisibility(View.GONE);
                tvSave.setVisibility(View.VISIBLE);
            }
        });
        tvSave = (TextView) findViewById(R.id.tv_commit);
        tvSave.setVisibility(View.GONE);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
                switchEditMode(false);
                tvEdit.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
            }
        });
        tvAccount = (EditText) findViewById(R.id.tv_account);
        tvNick = (EditText) findViewById(R.id.tv_nick);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUserInfo() {
        UserInfo user = curUser;
        user.setNick(tvNick.getText().toString());
        user.setName(etName.getText().toString().isEmpty() ? "" : etName.getText().toString());
        user.setPhone(etPhone.getText().toString().isEmpty() ? "" : etPhone.getText().toString());
        user.setAddress(etAddress.getText().toString().isEmpty() ? "" : etAddress.getText().toString());
        user.setIdCard(etIdcard.getText().toString().isEmpty() ? "" : etIdcard.getText().toString());
        user.setSex(etSex.getText().toString().isEmpty() ? "" : etSex.getText().toString());
        user.update(this, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), " >.< 更新失败，貌似出了点问题！", Toast.LENGTH_SHORT).show();
                Log.w(StaticUtil.TAG, "error:" + i + s);
            }
        });
    }

    private void switchEditMode(boolean editable) {
        tvNick.setEnabled(editable);
        etName.setEnabled(editable);
        etPhone.setEnabled(editable);
        etAddress.setEnabled(editable);
        etIdcard.setEnabled(editable);
        etSex.setEnabled(editable);
    }
}
