package com.dashu.buyer.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Comment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Smeiling on 2017/5/13.
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> commentList;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<Comment> list) {
        mContext = context;
        commentList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_comment, null);
        TextView time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView comment = (TextView) convertView.findViewById(R.id.tv_comment);

        Comment com = commentList.get(position);
        time.setText(com.getTime());
        comment.setText(com.getContent());
        return convertView;
    }
}
