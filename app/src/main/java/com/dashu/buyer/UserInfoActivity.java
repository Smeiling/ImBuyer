package com.dashu.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dashu.buyer.bean.User;

import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends Activity {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private EditText etNick;
    private EditText etCard;
    private EditText etAddress;
    private EditText etAccount;
    private EditText etPhone;
    private TextView tvSaveBtn;
    private String curAccount;
    private User curUser;
    private String objID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_info);
        initContentView();
        Intent intent = getIntent();
        curAccount = intent.getStringExtra("cur_account");
        objID = intent.getStringExtra("cur_object_id");
        etAccount.setText(curAccount);
        etPhone.setText(curAccount);

    }

    private void initContentView() {
        etNick = (EditText) findViewById(R.id.edit_nick);
        etCard = (EditText) findViewById(R.id.edit_card);
        etAddress = (EditText) findViewById(R.id.edit_address);
        etAccount = (EditText) findViewById(R.id.tv_account);
        etAccount.setEnabled(false);
        etPhone = (EditText) findViewById(R.id.show_phone);
        etPhone.setEnabled(false);
        tvSaveBtn = (TextView) findViewById(R.id.btn_select_save);
        tvSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curUser = new User();
                updateInfo();

            }
        });
    }

    private void updateInfo() {
        curUser.setCard(etCard.getText().toString().isEmpty() ? "" : etCard.getText().toString());
        curUser.setNick(etNick.getText().toString().isEmpty() ? curAccount : etNick.getText().toString());

        curUser.update(this, objID, new UpdateListener() {

            @Override
            public void onSuccess() {
                Log.i("bmob", "更新成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("bmob", "更新失败：" + s);
            }

        });
    }

}
