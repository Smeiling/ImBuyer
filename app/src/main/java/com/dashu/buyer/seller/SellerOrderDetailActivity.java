package com.dashu.buyer.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a.a.I;
import com.a.a.V;
import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.buyer.OrderConfirmActivity;
import com.dashu.buyer.buyer.RefundActivity;
import com.dashu.buyer.util.ToastUtils;

import cn.bmob.v3.listener.UpdateListener;

public class SellerOrderDetailActivity extends Activity implements View.OnClickListener {

    private TextView orderState;
    private TextView deliverDetail;
    private TextView receiverTv;
    private TextView receiverPhone;
    private TextView receiverAddress;
    private TextView orderId;
    private TextView orderDetail;
    private TextView orderPrice;
    private TextView orderTime;
    private Button returnBtn;
    private Button confirmBtn;
    private ImageView goodImg;
    private TextView tvCorp;
    private TextView tvCorpId;
    private LinearLayout corpLayout;
    private Button tvComment;


    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_seller_order_detail);
        mOrder = (Order) getIntent().getSerializableExtra("data");
        initView();
        fillContent();
    }

    private void fillContent() {
        if (mOrder.getOrderState() == 0) {
            orderState.setText("等待发货");
            returnBtn.setVisibility(View.INVISIBLE);
        } else if (mOrder.getOrderState() == 1) {

            orderState.setText("等待买家确认收货");
            returnBtn.setVisibility(View.INVISIBLE);

            confirmBtn.setVisibility(View.GONE);
            deliverDetail.setVisibility(View.GONE);
            corpLayout.setVisibility(View.INVISIBLE);
        } else if (mOrder.getOrderState() == 3) {
            orderState.setText("买家申请退货，请处理");
            confirmBtn.setVisibility(View.GONE);
            deliverDetail.setVisibility(View.GONE);
            corpLayout.setVisibility(View.INVISIBLE);
        } else {
            orderState.setText("交易已结束");
            returnBtn.setVisibility(View.GONE);
            confirmBtn.setVisibility(View.GONE);
            if (mOrder.getRbComment() < 7) {
                tvComment.setVisibility(View.VISIBLE);
            }
            deliverDetail.setVisibility(View.GONE);
            corpLayout.setVisibility(View.INVISIBLE);
        }
        receiverTv.setText("收件人：" + mOrder.getReceiver());
        receiverPhone.setText("联系方式：" + mOrder.getPhone());
        receiverAddress.setText("收货地址：" + mOrder.getAddress());
        orderId.setText(mOrder.getObjectId());
        orderDetail.setText(mOrder.getName());
        orderPrice.setText(String.valueOf(mOrder.getPrice()));
        orderTime.setText(mOrder.getSendTime());
        if (mOrder.getPngUrl() == null || mOrder.getPngUrl().isEmpty()) {
            goodImg.setImageResource(R.mipmap.product_default);
            goodImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Glide.with(this).load(mOrder.getPngUrl()).into(goodImg);
        }
    }

    private void initView() {
        orderState = (TextView) findViewById(R.id.tv_order_state);
        deliverDetail = (TextView) findViewById(R.id.delivery_detail);
        receiverTv = (TextView) findViewById(R.id.tv_receiver);
        receiverPhone = (TextView) findViewById(R.id.tv_phone);
        receiverAddress = (TextView) findViewById(R.id.tv_address);
        orderId = (TextView) findViewById(R.id.textview_orderId);
        orderDetail = (TextView) findViewById(R.id.textview_name);
        orderPrice = (TextView) findViewById(R.id.textview_money);
        orderTime = (TextView) findViewById(R.id.textview_time);
        goodImg = (ImageView) findViewById(R.id.imageView1);
        tvCorp = (TextView) findViewById(R.id.editText_input_corp);
        tvCorpId = (TextView) findViewById(R.id.editText_input_corpid);
        returnBtn = (Button) findViewById(R.id.tv_return_btn);
        confirmBtn = (Button) findViewById(R.id.tv_confirm_btn);
        corpLayout = (LinearLayout) findViewById(R.id.corp_layout);
        tvComment = (Button) findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.tv_return_btn:
                confirmReturn();
                break;
            case R.id.tv_confirm_btn:
                updateOrderInfo();
                break;
            case R.id.tv_comment:
                addComment();
                break;
        }
    }

    private void addComment() {
        Intent intent = new Intent(getApplicationContext(), SellerCommentActivity.class);
        intent.putExtra("order", mOrder);
        startActivityForResult(intent, 1);
    }

    private void confirmReturn() {
        mOrder.setOrderState(2);
        mOrder.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.w("TAG", "order close");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.w("TAG", "order" + i + s);
            }
        });
    }

    private void updateOrderInfo() {
        if (tvCorp.getText().toString() == null || tvCorp.getText().toString().isEmpty()
                || tvCorpId.getText().toString() == null || tvCorpId.getText().toString().isEmpty()) {
            Toast.makeText(this, "物流信息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            mOrder.setCorp(tvCorp.getText().toString());
            mOrder.setCorpId(tvCorpId.getText().toString());
            mOrder.setOrderState(1);
            mOrder.update(this, mOrder.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.w("TAG", i + s);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            tvComment.setVisibility(View.GONE);
        }
    }
}
