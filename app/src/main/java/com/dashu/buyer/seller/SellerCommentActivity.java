package com.dashu.buyer.seller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Order;

import cn.bmob.v3.listener.UpdateListener;

public class SellerCommentActivity extends Activity {

    private RadioButton rbGood;
    private RadioButton rbMid;
    private RadioButton rbBad;
    private EditText tvComment;
    private Button btnCommit;
    private Button btnBack;

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_comment);
        mOrder = (Order) getIntent().getSerializableExtra("order");
        initView();
    }

    private void initView() {
        rbGood = (RadioButton) findViewById(R.id.rb_good);
        rbMid = (RadioButton) findViewById(R.id.rb_mid);
        rbBad = (RadioButton) findViewById(R.id.rb_bad);
        tvComment = (EditText) findViewById(R.id.tv_comment);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });
        btnCommit = (Button) findViewById(R.id.btn_commit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });
    }

    private void showNormalDialog() {
        mOrder.setComment(tvComment.getText().toString());
        if (rbGood.isChecked()) {
            mOrder.setRbComment(7);
        } else if (rbMid.isChecked()) {
            mOrder.setRbComment(8);
        } else {
            mOrder.setRbComment(9);
        }
        mOrder.setOrderState(2);
        mOrder.update(getApplicationContext(), mOrder.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.w("TAG", "评价成功！");
                setResult(1);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.w("TAG", "评价失败！");
            }
        });
    }
}
