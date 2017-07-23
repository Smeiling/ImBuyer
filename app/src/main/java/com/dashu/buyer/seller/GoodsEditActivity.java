package com.dashu.buyer.seller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.db.DBHelper;
import com.lidroid.xutils.BitmapUtils;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 *
 */
public class GoodsEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoodsEditActivity";

    String gameid;
    Goods mGoods;
    TextView nameTextView;
    TextView typeTextView;
    TextView weightTextView;
    TextView priceTextView;
    TextView addressTextView;
    TextView timeTextView;
    TextView contentTextView;
    ImageView mImageView;
    BitmapUtils mBitmapUtils;
    TextView editDetailBtn;
    TextView deleteGoodBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_edit);
        mBitmapUtils = new BitmapUtils(this);
        mGoods = (Goods) getIntent().getSerializableExtra("data");
        mImageView = (ImageView) findViewById(R.id.imageview_detail);
        mBitmapUtils.display(mImageView, mGoods.getPngUrl());
        InitTextView();
        initContent();
    }

    private void InitTextView() {
        nameTextView = (TextView) findViewById(R.id.textView_name);
        typeTextView = (TextView) findViewById(R.id.textView_type);
        weightTextView = (TextView) findViewById(R.id.textView_weight);
        priceTextView = (TextView) findViewById(R.id.textView_price);
        addressTextView = (TextView) findViewById(R.id.textView_address);
        timeTextView = (TextView) findViewById(R.id.textView_date);
        contentTextView = (TextView) findViewById(R.id.textView_content);
        editDetailBtn = (TextView) findViewById(R.id.edit_detail);
        deleteGoodBtn = (TextView) findViewById(R.id.delete_good);
        editDetailBtn.setOnClickListener(this);
        deleteGoodBtn.setOnClickListener(this);
    }


    private void initContent() {
        nameTextView.setText(getString(R.string.detail_brief_version) + mGoods.getName());
        typeTextView.setText(getString(R.string.detail_brief_type) + mGoods.getType());
        weightTextView.setText(getString(R.string.detail_brief_weight) + mGoods.getWeight());
        priceTextView.setText(getString(R.string.detail_brief_price) + mGoods.getPrice());
        timeTextView.setText(getString(R.string.detail_brief_time) + mGoods.getDate());
        addressTextView.setText(getString(R.string.detail_brief_address) + mGoods.getAddress());
        contentTextView.setText(mGoods.getContent());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_detail:
                Toast.makeText(this, R.string.add_coll_success, Toast.LENGTH_SHORT).show();
                mGoods.update(this, mGoods.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getApplicationContext(), R.string.update_failure, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.delete_good:
                Toast.makeText(this, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
                mGoods.delete(this, mGoods.getObjectId(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getApplicationContext(), R.string.delete_failure, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
