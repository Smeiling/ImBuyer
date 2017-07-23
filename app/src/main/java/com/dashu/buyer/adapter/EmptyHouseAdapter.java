package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class EmptyHouseAdapter extends BaseAdapter {

    Context context;
    List<Goods>  list;
    public EmptyHouseAdapter(Context context, List<Goods> emptyList){
        this.context = context;
        this.list = emptyList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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

        Goods goods = (Goods) getItem(position);
        String text = String.format("房子:%1$s 面积： %2$2f 平方", goods.getType(), goods.getBrand());
        holder.houseNameTV.setText(text);
        return convertView;
    }


    final class ViewHolder{
        TextView houseNameTV;
    }
}
