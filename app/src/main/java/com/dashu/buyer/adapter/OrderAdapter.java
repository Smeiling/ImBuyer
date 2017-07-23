package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.db.DBHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    Context mContext;
    List<Order> mList;
    LayoutInflater mInflater;
    BitmapUtils mBitmapUtils;
    DBHelper dbHelper;

    public OrderAdapter(Context mContext, List<Order> mList2) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mList = mList2;
        mInflater = LayoutInflater.from(mContext);
        mBitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Order getItem(int position) {
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
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, null);
            item.time = (TextView) convertView.findViewById(R.id.textview_time);
            item.money = (TextView) convertView.findViewById(R.id.textview_money);
            item.orderId = (TextView) convertView.findViewById(R.id.textview_orderId);
            item.imgView = (ImageView) convertView.findViewById(R.id.imageView1);
            item.name = (TextView) convertView.findViewById(R.id.textview_name);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (mList.get(position).getPngUrl() == null || mList.get(position).getPngUrl().isEmpty()) {
            item.imgView.setImageResource(R.mipmap.product_default);
            item.imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Glide.with(mContext).load(mList.get(position).getPngUrl()).into(item.imgView);
        }
        item.time.setText(mList.get(position).getBuyDate());
        item.money.setText(mList.get(position).getPrice() + "");
        item.orderId.setText(mList.get(position).getObjectId() + "");
        item.name.setText(mList.get(position).getName() == null ? "" : mList.get(position).getName());
        return convertView;
    }

    private class Item {
        TextView time, money, orderId, name;
        ImageView imgView;
    }

}
