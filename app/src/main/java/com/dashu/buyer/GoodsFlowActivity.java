package com.dashu.buyer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.adapter.GoodsFlowAdapter;
import com.dashu.buyer.bean.ItemBean;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsFlowActivity extends AppCompatActivity {

    ListView listView;
    Order order;
    TextView emptyTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_flow);
        listView = (ListView) findViewById(R.id.listview_goods_flow);
        emptyTV = (TextView) findViewById(R.id.empty_tv);
        listView.setEmptyView(emptyTV);
        order = (Order) getIntent().getSerializableExtra("data");
        initListView(order);


    }

    GoodsFlowAdapter orderAdapter;
    private void initListView(Order order) {
        List<ItemBean>  list = new ArrayList<>();

        //
        ItemBean bean = new ItemBean();
        bean.setTitle("购买");
        bean.setTime(order.getBuyDate());
        bean.setStatu(1);
        list.add(bean);

        boolean isSend = order.isSend();
        if(!isSend){
            //未发货
            bean = new ItemBean();
            bean.setTitle("未发货");
            bean.setTime(order.getSendTime());
            bean.setStatu(0);
            list.add(bean);
        }else{
            //已发货
            bean = new ItemBean();
            bean.setTitle("已发货");
            bean.setTime(order.getSendTime());
            bean.setStatu(0);
            list.add(bean);

            bean = new ItemBean();
            bean.setTitle("在路上");
            bean.setTime(ToastUtils.getNowTime());
            list.add(bean);
        }

        orderAdapter = new GoodsFlowAdapter(this,list);
        listView.setAdapter(orderAdapter);
    }
}
