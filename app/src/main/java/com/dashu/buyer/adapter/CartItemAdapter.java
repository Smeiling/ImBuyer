package com.dashu.buyer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.ShopingGoods;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public class CartItemAdapter extends BaseAdapter {
    Context mContext;
    List<ShopingGoods> mList;
    Holder mHolder;
    LayoutInflater mInflater;
    BitmapUtils bitmapUtils;
    private List<String> sellerIds;
    private List<String> sellerNames;
    private List<Integer> positions;

    public CartItemAdapter(Context mContext, List<ShopingGoods> mList2) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mList = mList2;
        mInflater = LayoutInflater.from(mContext);
        bitmapUtils = new BitmapUtils(mContext);
    }

    public void update(List<ShopingGoods> list) {
        mList = list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ShopingGoods getItem(int position) {
        // TODO Auto-generated method stub
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ShopingGoods mShopingGoods = getItem(position);
        if (null != mShopingGoods.getOwnerObjId()) {
            convertView = mInflater.inflate(R.layout.item_shopping_good, null);
            mHolder = new Holder();
            mHolder.iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
            mHolder.nameTextView = (TextView) convertView.findViewById(R.id.textView1);
            mHolder.priceTextView = (TextView) convertView.findViewById(R.id.textView2);
            mHolder.numberTextView = (TextView) convertView.findViewById(R.id.textView3);
            mHolder.resultTextView = (TextView) convertView.findViewById(R.id.textView4);

            //bitmapUtils.display(mHolder.iconImageView, mShopingGoods.getPngUrl());
            if (mShopingGoods.getPngUrl() == null || mShopingGoods.getPngUrl().isEmpty()) {
                mHolder.iconImageView.setImageResource(R.mipmap.product_default);
                mHolder.iconImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                Glide.with(mContext).load(mShopingGoods.getPngUrl()).into(mHolder.iconImageView);
            }
            mHolder.nameTextView.setText("名称：" + mShopingGoods.getGoods_name());
            mHolder.priceTextView.setText("价格：" + mShopingGoods.getsPrice() + "");
            mHolder.numberTextView.setText("数量:" + mShopingGoods.getToBuyNum());
            float result = mShopingGoods.getToBuyNum() * mShopingGoods.getsPrice();
            mHolder.resultTextView.setText(result + " 元");
        } else {
            convertView = mInflater.inflate(R.layout.item_cart_title, null);
            TextView tv = (TextView) convertView.findViewById(R.id.textView1);
            tv.setText(mShopingGoods.getGoods_name());
            convertView.setClickable(false);
        }

        return convertView;
    }

    public void deleteItem(ShopingGoods mGoods) {
        mList.remove(mGoods);
        this.notifyDataSetChanged();
    }

    class Holder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView numberTextView;
        TextView resultTextView;
    }
}
