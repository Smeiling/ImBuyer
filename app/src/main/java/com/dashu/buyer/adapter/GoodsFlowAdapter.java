package com.dashu.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

public class GoodsFlowAdapter extends BaseAdapter{
	private Context mContext;
	private List<ItemBean> datas = new ArrayList<ItemBean>();

	public GoodsFlowAdapter(Context mContext, List<ItemBean> datas) {
		super();
		this.mContext = mContext;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
			item.time = (TextView) convertView.findViewById(R.id.show_time);
			item.title= (TextView) convertView.findViewById(R.id.show_title);
			item.line = convertView.findViewById(R.id.line_normal);
			convertView.setTag(item);
		} else {
			item = (Item) convertView.getTag();
		}

		item.time.setText(datas.get(position).getTime());
		item.title.setText(datas.get(position).getTitle());

		//最后一项时，竖线不再显示
		if (position == datas.size() - 1) {
			item.line.setVisibility(View.GONE);
		}else{
			item.line.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private class Item {
		TextView time, title;
		View line;
	}

}
