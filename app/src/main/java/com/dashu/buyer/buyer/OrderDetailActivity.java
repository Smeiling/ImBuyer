package com.dashu.buyer.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a.a.I;
import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.Order;

import org.w3c.dom.Text;

public class OrderDetailActivity extends Activity implements View.OnClickListener {

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
    private TextView tvSender;

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_order_detail);
        mOrder = (Order) getIntent().getSerializableExtra("data");
        initView();
        fillContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fillContent() {
        if (mOrder.getOrderState() == 0) {
            orderState.setText("等待卖家发货");
            deliverDetail.setText("暂无信息");
            returnBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
        } else if (mOrder.getOrderState() == 1) {
            orderState.setText("卖家已发货");
            deliverDetail.setText(mOrder.getCorp() + " 快递单号：" + mOrder.getCorpId());
        } else if (mOrder.getOrderState() == 2) {
            orderState.setText("交易已结束");
            deliverDetail.setText(mOrder.getCorp() + " 快递单号：" + mOrder.getCorpId());
            returnBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
        } else if (mOrder.getOrderState() == 3) {
            orderState.setText("已申请退货，待处理");
            returnBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
            deliverDetail.setText(mOrder.getCorp() + " 快递单号：" + mOrder.getCorpId());
        }
        receiverTv.setText("收件人：" + mOrder.getReceiver());
        receiverPhone.setText("联系方式：" + mOrder.getPhone());
        receiverAddress.setText("收货地址：" + mOrder.getAddress());
        orderId.setText(mOrder.getObjectId());
        orderDetail.setText(mOrder.getName());
        orderPrice.setText(String.valueOf(mOrder.getPrice()));
        orderTime.setText(mOrder.getSendTime());
        tvSender.setText(mOrder.getSellerNick() == null ? "" : mOrder.getSellerNick());
        if (mOrder.getPngUrl() == null || mOrder.getPngUrl().isEmpty()) {
            goodImg.setImageResource(R.mipmap.product_default);
            goodImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Glide.with(this).load(mOrder.getPngUrl()).into(goodImg);
        }
    }

    private void initView() {
        orderState = (TextView) findViewById(R.id.tv_order_state);
        deliverDetail = (TextView) findViewById(R.id.car_corp);
        receiverTv = (TextView) findViewById(R.id.tv_receiver);
        receiverPhone = (TextView) findViewById(R.id.tv_phone);
        receiverAddress = (TextView) findViewById(R.id.tv_address);
        orderId = (TextView) findViewById(R.id.textview_orderId);
        orderDetail = (TextView) findViewById(R.id.textview_name);
        orderPrice = (TextView) findViewById(R.id.textview_money);
        orderTime = (TextView) findViewById(R.id.textview_time);
        goodImg = (ImageView) findViewById(R.id.imageView1);
        tvSender = (TextView) findViewById(R.id.sender);
        returnBtn = (Button) findViewById(R.id.tv_return_btn);
        confirmBtn = (Button) findViewById(R.id.tv_confirm_btn);
        returnBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.tv_return_btn:
                mIntent = new Intent(this, RefundActivity.class);
                mIntent.putExtra("data", mOrder);
                startActivityForResult(mIntent, 1);
                break;
            case R.id.tv_confirm_btn:
                mIntent = new Intent(this, OrderConfirmActivity.class);
                mIntent.putExtra("data", mOrder);
                startActivityForResult(mIntent, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            orderState.setText("已申请退货，待处理");
            returnBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
        } else if (requestCode == 2) {
            orderState.setText("交易已结束");
            deliverDetail.setText(mOrder.getCorp() + " 快递单号：" + mOrder.getCorpId());
            returnBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
        }
    }
}
