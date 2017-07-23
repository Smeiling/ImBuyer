package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/12/15.
 */
public class NeedPayAdapter extends BaseAdapter {

    Context context;
    List<User>  list;
    public NeedPayAdapter(Context context, List<User> emptyList){
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

    ViewHolder holder = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.frg_pay_item,null);
            holder = new ViewHolder();
            holder.houseNameTV = (TextView) convertView.findViewById(R.id.house_item);
            holder.noticeButton = (Button) convertView.findViewById(R.id.notice_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final User rentMan = (User) getItem(position);
//        String text = String.format("租客:%1$s \n 房间： %2$s",rentMan.getUsername(),rentMan.getHouseNumber());
//        holder.houseNameTV.setText(text);
//        if(rentMan.isNeedPayMoney()){
//            holder.noticeButton.setText("已通知");
//            holder.noticeButton.setEnabled(false);
//        }else{
//            holder.noticeButton.setText("通知");
//            holder.noticeButton.setEnabled(true);
//        }
        holder.noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rentMan.setNeedPayMoney(true);
                rentMan.update(context, rentMan.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showTip(context,"已通知");
                        holder.noticeButton.setText("已通知");

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.showTip(context,s);

                    }
                });

            }
        });
        return convertView;
    }


    final class ViewHolder{
        TextView houseNameTV;
        Button noticeButton;
    }
}
