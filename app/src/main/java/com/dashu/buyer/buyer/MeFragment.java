package com.dashu.buyer.buyer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.OrderAdapter;
import com.dashu.buyer.bean.Order;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * @author 123
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MeFragment extends Fragment implements View.OnClickListener {

    private int fragmentId = 0;

    private ListView orderListView;
    private TextView tvNick;
    private ImageView ivAccountInfo;
    private List<Order> orderList;

    //tab
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private TextView tab4;
    private View line1;
    private View line2;
    private View line3;
    private View line4;
    private CustomProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w("TAG", "onCreateView");
        return inflater.inflate(R.layout.fragment_me_buyer, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w("TAG", "onActivityCreated");
        initContentView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("TAG", "onResume");
        doQuery(fragmentId);
    }

    private void doQuery(int type) {
        Log.w("TAG", "doQuery");
        if (orderListView != null) {
            orderListView.setAdapter(null);
        }
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        BmobQuery<Order> query = new BmobQuery<Order>();
        query.addWhereEqualTo("buyerId", StaticUtil.currentUser.getObjectId());
        query.addWhereEqualTo("orderState", type);
        query.findObjects(getActivity(), new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    fillList(object);
                } else {
                    fillList(object);
                    //ToastUtils.showTip(getActivity(), "还没商品录入：");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.w(StaticUtil.TAG, "查询失败：" + msg);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }

    private void initContentView() {
        Log.w("TAG", "initContentView");
        tvNick = (TextView) getView().findViewById(R.id.tv_account_nick);
        tvNick.setText(StaticUtil.currentUser.getNick() + ", 您好！");
        ivAccountInfo = (ImageView) getView().findViewById(R.id.iv_account_info);
        tab1 = (TextView) getView().findViewById(R.id.tv_1);
        tab2 = (TextView) getView().findViewById(R.id.tv_2);
        tab3 = (TextView) getView().findViewById(R.id.tv_4);
        tab4 = (TextView) getView().findViewById(R.id.tv_3);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        line1 = getView().findViewById(R.id.line_1);
        line2 = getView().findViewById(R.id.line_2);
        line3 = getView().findViewById(R.id.line_3);
        line4 = getView().findViewById(R.id.line_4);
        ivAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: start account info activity
                Intent mIntent = new Intent(getActivity(), AccountInfoActivity.class);
                startActivity(mIntent);
            }
        });
        orderListView = (ListView) getView().findViewById(R.id.lv_order_list);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), OrderDetailActivity.class);
                mIntent.putExtra("data", orderList.get(position));
                startActivity(mIntent);
            }
        });

        switchTabList(true, false, false, false);
    }

    private void fillList(List<Order> object) {
        if (object.size() < 1) {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.GONE);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        orderList = object;
        OrderAdapter gAdapter = new OrderAdapter(getActivity(), object);
        orderListView.setAdapter(gAdapter);
    }


    private void switchTabList(boolean one, boolean two, boolean three, boolean four) {
        Log.w("TAG", "switchTabList");
        if (one) {
            line1.setVisibility(View.VISIBLE);
        } else {
            line1.setVisibility(View.INVISIBLE);
        }
        if (two) {
            line2.setVisibility(View.VISIBLE);
        } else {
            line2.setVisibility(View.INVISIBLE);
        }
        if (three) {
            line3.setVisibility(View.VISIBLE);
        } else {
            line3.setVisibility(View.INVISIBLE);
        }
        if (four) {
            line4.setVisibility(View.VISIBLE);
        } else {
            line4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                //fillTodoOrder();
                fragmentId = 0;
                doQuery(fragmentId);
                switchTabList(true, false, false, false);
                break;
            case R.id.tv_2:
                fragmentId = 1;
                doQuery(fragmentId);
                //fillReturnOrder();
                switchTabList(false, true, false, false);
                break;
            case R.id.tv_3:
                fragmentId = 3;
                doQuery(fragmentId);
                //fillAllOrder();
                switchTabList(false, false, true, false);
                break;
            case R.id.tv_4:
                fragmentId = 2;
                doQuery(fragmentId);

                //fillAllOrder();
                switchTabList(false, false, false, true);
                break;
        }
    }
}
