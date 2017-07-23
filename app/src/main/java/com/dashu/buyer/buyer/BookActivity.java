package com.dashu.buyer.buyer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.CartBean;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.bean.ShopingCar;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author 123
 */
public class BookActivity extends AppCompatActivity {

    public final static int SPLASH_DISPLAY_LENGHT = 2000;
    List<String> imageList;
    ProgressDialog pDialog = null;
    boolean validate = false;
    String account = null;
    String password = null;

    Button buyButton;
    int totalNum;
    float totalPrice;
    String time = null;
    String address = null;
    String defaulttime = null;
    String defaultaddress = null;
    DBHelper mDbHelper;
    Calendar c;
    private EditText etReceiver;
    private EditText etPhone;
    private EditText etAddress;
    private String imgUrl;
    private String orderName;
    private RadioButton rbCard;
    private RadioButton rbAli;
    private RadioButton rbWechat;

    private EditText moneyET;

    private ShopingCar mCar;
    public List<ShopingGoods> mList = null;
    private List<Map<CartBean, List<ShopingGoods>>> totalList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        etReceiver = (EditText) findViewById(R.id.et_receiver);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        rbCard = (RadioButton) findViewById(R.id.rb_card);
        rbAli = (RadioButton) findViewById(R.id.rb_alipay);
        rbWechat = (RadioButton) findViewById(R.id.rb_wechat);

        String receiver = "";
        String mobile = "";
        String address = "";
        if (null != StaticUtil.currentUser.getReceiver()) {
            if (!StaticUtil.currentUser.getReceiver().isEmpty()) {
                receiver = StaticUtil.currentUser.getReceiver();
            }
        }
        if (null != StaticUtil.currentUser.getMobilePhoneNumber()) {
            if (!StaticUtil.currentUser.getMobilePhoneNumber().isEmpty()) {
                mobile = StaticUtil.currentUser.getMobilePhoneNumber();
            }
        }
        if (null != StaticUtil.currentUser.getAddress()) {
            if (!StaticUtil.currentUser.getAddress().isEmpty()) {
                address = StaticUtil.currentUser.getAddress();
            }
        }

        etReceiver.setText(receiver);
        etPhone.setText(mobile);
        etAddress.setText(address);

        Bundle mBundle = getIntent().getExtras();
        totalNum = mBundle.getInt("totalNum");
        totalPrice = mBundle.getFloat("totalPrice");
        totalList = (List<Map<CartBean, List<ShopingGoods>>>) mBundle.getSerializable("orderName");
        mList = CartFragment.mList;

        buyButton = (Button) findViewById(R.id.book_submit);
        mDbHelper = DBHelper.getInstance(this);

        buyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (etReceiver.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty() || etAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "收货信息不完整！", Toast.LENGTH_SHORT).show();
                } else if (rbWechat.isChecked() == false && rbAli.isChecked() == false && rbCard.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "请选择支付方式！", Toast.LENGTH_SHORT).show();
                } else {
                    showPayDialog(totalPrice);
                }
            }
        });
    }

    public void showPayDialog(float totalPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pay_num);
        moneyET = new EditText(this);
        moneyET.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(moneyET);
        moneyET.setText(totalPrice + "");
        moneyET.setEnabled(false);
        builder.setPositiveButton(R.string.pay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String money = moneyET.getText().toString();
                if (!TextUtils.isEmpty(money)) {
                    createOrder();
                }
            }
        });
        builder.setNegativeButton(R.string.pay_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create();
        builder.show();
    }


    private void doSaveOrder(ShopingCar shopingCar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        Order order = new Order();
        order.setAddress(shopingCar.getAddress());
        order.setBuyDate(ToastUtils.getNowTime());
        order.setPrice(shopingCar.getTotalPrice());
        order.setBuyerId(StaticUtil.currentUser.getObjectId());
        order.setReceiver(etReceiver.getText().toString());
        order.setPhone(etPhone.getText().toString());
        order.setAddress(etAddress.getText().toString());
        order.setOrderState(0);//not send yet
        order.setSendTime(time);
        order.setCommented(false);
        order.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
//				ToastUtils.showTip(BookActivity.this,"add ok");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showTip(BookActivity.this, "add wrong : " + s);
            }
        });


    }


    private void doMakeOrder(String sellerId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        Order order = new Order();
        order.setAddress(mCar.getAddress());
        order.setBuyDate(ToastUtils.getNowTime());
        order.setPngUrl(imgUrl);
        order.setPrice(mCar.getTotalPrice());
        order.setBuyerId(StaticUtil.currentUser.getObjectId());
        order.setSellerId(sellerId);
        order.setReceiver(etReceiver.getText().toString());
        order.setPhone(etPhone.getText().toString());
        order.setAddress(etAddress.getText().toString());
        order.setName(orderName);
        order.setOrderState(0);//not send yet
        order.setSendTime(time);
        order.setCommented(false);
        order.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
//				ToastUtils.showTip(BookActivity.this,"add ok");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showTip(BookActivity.this, "add wrong : " + s);
            }
        });


    }

    private void clearCart(List<ShopingGoods> list) {
        for (ShopingGoods good : list) {
            good.delete(this, new DeleteListener() {
                @Override
                public void onSuccess() {
                    Log.d("TAG", "deleted");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("TAG", "delete fail " + i + s);
                }
            });
        }
        setResult(1);
        finish();
    }


    void createOrder() {
        int i;
        List<ShopingGoods> list = new ArrayList<>();
        for (i = 0; i < totalList.size(); i++) {

            Order order = new Order();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            order.setBuyDate(ToastUtils.getNowTime());
            Iterator iter = totalList.get(i).entrySet().iterator();
            Object key = null;
            Object val = null;
            if (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key = entry.getKey();
                val = entry.getValue();
            }
            order.setPngUrl(((List<ShopingGoods>) val).get(0).getPngUrl());
            order.setPrice(totalPrice);
            order.setBuyerId(StaticUtil.currentUser.getObjectId());
            order.setSellerId(((CartBean) key).getSellerObjId());
            order.setSellerNick(((CartBean) key).getSellerName());
            order.setReceiver(etReceiver.getText().toString());
            order.setPhone(etPhone.getText().toString());
            order.setAddress(etAddress.getText().toString());
            order.setName(((List<ShopingGoods>) val).get(0).getGoods_name());
            order.setOrderState(0);//not send yet
            order.setSendTime(time);
            order.setCommented(false);
            list.addAll((List<ShopingGoods>) val);
            order.save(getApplicationContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.showTip(BookActivity.this, "add success");
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.showTip(BookActivity.this, "add wrong : " + s);
                }
            });
        }

        clearCart(list);


/*
        mCar = new ShopingCar();
        mCar.setAddress(address);
        mCar.setTime(time);
        mCar.setTotalNum(totalNum);
        mCar.setTotalPrice(totalPrice);
        mCar.setAccount(account);

        doSplitOrder();
        BmobQuery<ShopingGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(this, new FindListener<ShopingGoods>() {
            @Override
            public void onSuccess(List<ShopingGoods> list) {
                clearCart(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });*/

        finish();

    }

    private void doSplitOrder() {
        BmobQuery<ShopingGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.groupby(new String[]{"sellerObjId"});
        query.findStatistics(this, ShopingGoods.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object object) {
                JSONArray ary = (JSONArray) object;
                if (ary != null) {//
                    try {
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject obj = ary.getJSONObject(i);
                            String sellerId = obj.getString("sellerObjId");
                            Log.w("TAG", "sellerID = " + sellerId);
                            doMakeOrder(sellerId);
                        }
                    } catch (JSONException e) {
                    }
                } else {
                    //showToast("查询成功，无数据");
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
