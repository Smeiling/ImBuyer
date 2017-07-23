package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Conver;
import com.dashu.buyer.im.bean.Conversation;

import java.security.acl.Owner;
import java.util.List;

/**
 * Created by Smeiling on 2017/5/8.
 */

public class ChatLogAdapter extends BaseAdapter {

    private Context mContext;
    private List<Conver> converList;
    private LayoutInflater inflater;

    public ChatLogAdapter(Context context, List<Conver> list) {
        mContext = context;
        converList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return converList.size();
    }

    @Override
    public Object getItem(int position) {
        return converList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_chat_log, null);
        TextView buyer = (TextView) convertView.findViewById(R.id.buyer);
        TextView seller = (TextView) convertView.findViewById(R.id.seller);
        Conver con = converList.get(position);
        buyer.setText(con.getBuyer());
        seller.setText(con.getSeller());
        return convertView;
    }
}
