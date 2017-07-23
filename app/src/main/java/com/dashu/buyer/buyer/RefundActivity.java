package com.dashu.buyer.buyer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Order;

import cn.bmob.v3.listener.UpdateListener;

public class RefundActivity extends Activity {

    private Spinner spinner;
    private EditText tvComment;
    private Button btnCommit;
    private Button btnBack;


    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        mOrder = (Order) getIntent().getSerializableExtra("data");
        initView();
    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.refund_spinner);
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
                if (spinner.getSelectedItemPosition() == 0) {
                    mOrder.setRbComment(3);
                } else if (spinner.getSelectedItemPosition() == 1) {
                    mOrder.setRbComment(4);
                } else if (spinner.getSelectedItemPosition() == 2) {
                    mOrder.setRbComment(5);
                } else {
                    mOrder.setRbComment(6);
                }
                mOrder.setOrderState(3);
                mOrder.update(getApplicationContext(), mOrder.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.w("TAG", "退货申请成功！");
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.w("TAG", "退货失败！");
                    }
                });


            }
        });
    }
}
