package com.dashu.buyer.admin;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashu.buyer.LoginActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.RegisterActivity;
import com.dashu.buyer.util.StaticUtil;

public class AdminMainActivity extends Activity {

    private ImageView ivUser;
    private ImageView ivSeller;
    private ImageView ivChat;
    private ImageView ivKefu;
    private TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initView();
    }

    private void initView() {
        ivUser = (ImageView) findViewById(R.id.iv_user);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
                startActivity(intent);
            }
        });
        ivSeller = (ImageView) findViewById(R.id.iv_seller);
        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                startActivity(intent);
            }
        });
        ivChat = (ImageView) findViewById(R.id.iv_chat);
        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:get chatlist
                //showNormalDialog();
                Intent intent = new Intent(getApplicationContext(), ChatLogActivity.class);
                startActivity(intent);
            }
        });
        ivKefu = (ImageView) findViewById(R.id.iv_kefu);
        ivKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), KefuActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticUtil.clearSP(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void doRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    private void showNormalDialog() {

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("");
        normalDialog.setMessage("功能暂未开放！");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
}
