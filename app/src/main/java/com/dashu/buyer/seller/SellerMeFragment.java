package com.dashu.buyer.seller;

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
import com.dashu.buyer.buyer.AccountInfoActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.view.CustomProgressDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * ���ﳵ
 *
 * @author 123
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SellerMeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = SellerMeFragment.class.getSimpleName();
    OrderAdapter orderAdapter;
    private TextView tvNick;
    private ImageView ivAccountInfo;
    private int fragmentId = 0;
    //tab
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private TextView tab4;
    private View line1;
    private View line2;
    private View line3;
    private View line4;
    private ListView todoOrderList;

    private List<Order> todoOrders;
    private CustomProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_seller, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContentView(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        doQuery(fragmentId);
    }


    private void initContentView(View view) {
        todoOrderList = (ListView) view.findViewById(R.id.lv_todo_order);
        todoOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), SellerOrderDetailActivity.class);
                mIntent.putExtra("data", todoOrders.get(position));
                startActivity(mIntent);
            }
        });
        tvNick = (TextView) getView().findViewById(R.id.tv_account_nick);
        tvNick.setText(StaticUtil.currentUser.getNick() + ", 您好！");
        ivAccountInfo = (ImageView) getView().findViewById(R.id.iv_account_info);
        ivAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: start account info activity
                Intent mIntent = new Intent(getActivity(), AccountInfoActivity.class);
                startActivity(mIntent);
            }
        });
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
        switchTabList(true, false, false, false);
    }

    private void fillOrder(List<Order> list) {
        //queryResult = list;
        if (list.size() < 1) {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.GONE);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        todoOrders = list;
        orderAdapter = new OrderAdapter(getActivity(), list);
        todoOrderList.setAdapter(orderAdapter);
    }


    private void doQuery(int orderState) {
        Log.w("TAG", "doQuery");
        if (todoOrderList != null) {
            todoOrderList.setAdapter(null);
        }
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        BmobQuery<Order> query = new BmobQuery<Order>();
        query.addWhereEqualTo("orderState", orderState);
        query.addWhereEqualTo("sellerId", StaticUtil.currentUser.getObjectId());
        query.findObjects(getActivity(), new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    fillOrder(object);
                } else {
                    fillOrder(object);
                }
            }

            @Override
            public void onError(int code, String msg) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Log.d(TAG, "query onError " + code + msg);
            }
        });
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
