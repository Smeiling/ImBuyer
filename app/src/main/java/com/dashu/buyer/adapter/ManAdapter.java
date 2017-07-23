package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.User;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ManAdapter extends BaseAdapter {

    Context context;
    List<User>  list;
    public ManAdapter(Context context, List<User> emptyList){
        this.context = context;
        this.list = emptyList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public User getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.frg_house_item,null);
            holder = new ViewHolder();
            holder.houseNameTV = (TextView) convertView.findViewById(R.id.house_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        User house = (User) getItem(position);
//        String text = String.format("租客:%1$s 房间： %2$s",house.getUsername(),house.getHouseNumber());
//        holder.houseNameTV.setText(text+"\n电话："+house.getPhone());
        return convertView;
    }


    final class ViewHolder{
        TextView houseNameTV;
    }
}
