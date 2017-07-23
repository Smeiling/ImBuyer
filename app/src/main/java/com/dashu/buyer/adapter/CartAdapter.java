package com.dashu.buyer.adapter;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.dashu.buyer.R;

import java.util.List;

/**
 * Created by Smeiling on 2017/5/1.
 */

public class CartAdapter extends BaseAdapter {

    private Context mContext;
    private List<View> viewList;
    private LayoutInflater inflater;

    public CartAdapter(Context context, List<View> list) {
        mContext = context;
        viewList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_cart, null);
        RadioButton cbTitle = (RadioButton) convertView.findViewById(R.id.cb_seller_name);

        return null;
    }
}
