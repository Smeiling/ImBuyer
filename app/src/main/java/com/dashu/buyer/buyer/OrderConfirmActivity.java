package com.dashu.buyer.buyer;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Order;

import cn.bmob.v3.listener.UpdateListener;

public class OrderConfirmActivity extends Activity {

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
        setContentView(R.layout.content_order_confirm);
        mOrder = (Order) getIntent().getSerializableExtra("data");
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
                mOrder.setComment(tvComment.getText().toString());
                if (rbGood.isChecked()) {
                    mOrder.setRbComment(0);
                } else if (rbMid.isChecked()) {
                    mOrder.setRbComment(1);
                } else {
                    mOrder.setRbComment(2);
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
        });
    }

}
