package com.dashu.buyer.admin;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.GoodsAdapter;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.buyer.AccountInfoActivity;
import com.dashu.buyer.buyer.GoodsDetailActivity;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.im.ui.ChatActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Main cusumer page
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdminFragment extends Fragment implements View.OnClickListener {

    private int fragmentId = 0;

    //HomeAdapter mAdapter;
    private String[] titles;
    private List<Goods> list;
    private List<Goods> resultList;
    //tab
    //tabs
    private ListView lvCosList;

    private TextView cosTab;
    private TextView skTab;
    private TextView perfumeTab;
    private View cosLine;
    private View skLine;
    private View perfumeLine;
    private LinearLayout btnLayout;
    private TextView tvNoResult;

    private List<Goods> curList;
    private List<User> userList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frg_admin, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        doQuery(fragmentId);
    }

    private void initView(View view) {
        tvNoResult = (TextView) view.findViewById(R.id.tv_no_result);
        btnLayout = (LinearLayout) view.findViewById(R.id.btn_layout);
        lvCosList = (ListView) view.findViewById(R.id.listview_cos);
        lvCosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent;
                if (fragmentId == 1) {
                    mIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                    mIntent.putExtra("data", curList.get(position));
                } else {
                    mIntent = new Intent(getActivity(), AccountInfoActivity.class);
                    mIntent.putExtra("user", userList.get(position));
                }
                startActivity(mIntent);
            }
        });
        lvCosList.setVisibility(View.VISIBLE);

        cosTab = (TextView) view.findViewById(R.id.tv_cos_btn);
        skTab = (TextView) view.findViewById(R.id.tv_sk_btn);
        perfumeTab = (TextView) view.findViewById(R.id.tv_perfume_btn);

        cosTab.setOnClickListener(this);
        skTab.setOnClickListener(this);
        perfumeTab.setOnClickListener(this);
        cosLine = view.findViewById(R.id.cos_line);
        skLine = view.findViewById(R.id.sk_line);
        perfumeLine = view.findViewById(R.id.perfume_line);
        switchTabList(true, false, false);

    }

    private void doQuery(int type) {
        if (lvCosList != null) {
            lvCosList.setAdapter(null);
        }
        tvNoResult.setVisibility(View.GONE);
        if (type == 1) {
            BmobQuery<Goods> query = new BmobQuery<Goods>();
            query.findObjects(getActivity(), new FindListener<Goods>() {
                @Override
                public void onSuccess(List<Goods> object) {
                    // TODO Auto-generated method stub
                    if (object != null && object.size() > 0) {
                        Log.d("TAG", "object size = " + object.size());
                        fillCos(object);
                    } else {
                        fillCos(object);
                        ToastUtils.showTip(getActivity(), "还没商品录入：");
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    ToastUtils.showTip(getActivity(), "查询失败：" + msg);
                }
            });
        } else {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("roleType", type);
            query.findObjects(getActivity(), new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (list != null && list.size() > 0) {
                        Log.d("TAG", "object size = " + list.size());
                        fillUser(list);
                    } else {
                        fillUser(list);
                        ToastUtils.showTip(getActivity(), "还没商品录入：");
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

    }

    private void fillCos(List<Goods> obj) {
        if (obj.size() < 1) {
            tvNoResult.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.GONE);
        }
        curList = obj;
        GoodsAdapter gAdapter = new GoodsAdapter(getActivity(), obj);
        lvCosList.setAdapter(gAdapter);
    }

    private void fillUser(List<User> obj) {
        if (obj.size() < 1) {
            tvNoResult.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.GONE);
        }
        String[] account = new String[obj.size()];
        String[] nick = new String[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            account[i] = obj.get(i).getUsername();
            nick[i] = obj.get(i).getNick();
        }
        ArrayList<Map<String, Object>> mData = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", account[i]);
            item.put("text", nick[i]);
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mData, android.R.layout.simple_list_item_2, new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});

        userList = obj;
        lvCosList.setAdapter(adapter);
    }

    private void switchTabList(boolean cos, boolean sk, boolean perfume) {
        if (cos) {
            cosLine.setVisibility(View.VISIBLE);
        } else {
            cosLine.setVisibility(View.INVISIBLE);
        }
        if (sk) {
            skLine.setVisibility(View.VISIBLE);
        } else {
            skLine.setVisibility(View.INVISIBLE);
        }
        if (perfume) {
            perfumeLine.setVisibility(View.VISIBLE);
        } else {
            perfumeLine.setVisibility(View.INVISIBLE);
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cos_btn:
                fragmentId = 0;
                doQuery(fragmentId);
                switchTabList(true, false, false);
                break;
            case R.id.tv_sk_btn:
                fragmentId = 2;
                doQuery(fragmentId);
                switchTabList(false, true, false);
                break;
            case R.id.tv_perfume_btn:
                fragmentId = 1;
                doQuery(fragmentId);
                switchTabList(false, false, true);
                break;
        }
    }

}
