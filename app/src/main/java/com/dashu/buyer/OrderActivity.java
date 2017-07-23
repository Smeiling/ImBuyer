package com.dashu.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.adapter.OrderAdapter;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderActivity extends AppCompatActivity {

    ListView listView;
    TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        listView = (ListView) findViewById(R.id.listview_order);
        emptyTV = (TextView) findViewById(R.id.empty_tv);
        listView.setEmptyView(emptyTV);

        doQueryOrder();

    }

    private void doQueryOrder() {
        BmobQuery<Order> query = new BmobQuery<Order>();
        //查询playerName叫“比目”的数据
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(OrderActivity.this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    initListView(object);

                } else {
                    ToastUtils.showTip(OrderActivity.this, "还没有消费记录：");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
//                ToastUtils.showTip((OrderActivity.this, "查询失败：" + msg);
            }
        });
    }

    OrderAdapter orderAdapter;

    private void initListView(final List<Order> list) {
        orderAdapter = new OrderAdapter(this, list);
        listView.setAdapter(orderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(OrderActivity.this, GoodsFlowActivity.class).putExtra("data", list.get(position)));
            }
        });
    }
}
