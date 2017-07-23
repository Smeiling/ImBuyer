package com.dashu.buyer.buyer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.a.V;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dashu.buyer.buyer.GoodsDetailActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.adapter.GoodsAdapter;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.im.ui.ChatActivity;
import com.dashu.buyer.util.DataUtils;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import org.w3c.dom.Text;

import java.util.List;

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
public class GoodsFragment extends Fragment implements View.OnClickListener {

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
    private CustomProgressDialog progressDialog;

    private List<Goods> curList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frg_goods, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        //doQuery(fragmentId);
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
                Intent mIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                mIntent.putExtra("data", curList.get(position));
                startActivity(mIntent);
            }
        });
        lvCosList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                doChat(curList.get(position));
                return false;
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
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        if (lvCosList != null) {
            lvCosList.setAdapter(null);
        }
        tvNoResult.setVisibility(View.GONE);
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("type", type);
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                ToastUtils.showTip(getActivity(), "查询失败：" + msg);
            }
        });
    }

    private void fillCos(List<Goods> obj) {
        if (obj.size() < 1) {
            tvNoResult.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.GONE);
        }
        curList = obj;
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        GoodsAdapter gAdapter = new GoodsAdapter(getActivity(), obj);
        lvCosList.setAdapter(gAdapter);
        //progressDialog.dismiss();

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


    private void doChat(Goods goods) {
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(goods.getSellerObjectId());
        info.setName(goods.getSellerName());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    //在此跳转到聊天页面
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle);
                } else {
                    Log.w("TAG", e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void doCollectionGoods(Goods mGoods) {
        CollectionGoods collectionGoods = new CollectionGoods();
        collectionGoods.setName(mGoods.getName());
        collectionGoods.setPrice(mGoods.getPrice());
        collectionGoods.setBrand(mGoods.getBrand());
        collectionGoods.setPngUrl(mGoods.getPngUrl());
        collectionGoods.setType(String.valueOf(mGoods.getType()));
        boolean b = DBHelper.getInstance(getActivity()).addCollection(collectionGoods);
        if (b) {
            Toast.makeText(getActivity(), "收藏 " + mGoods.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_LONG).show();
        }
    }

    private void doAddShopCar(Goods mGoods) {
        Log.w("add ", mGoods.getName());
        /*ShopingGoods mShopingGoods = new ShopingGoods();
        mShopingGoods.setGoods_name(mGoods.getName());
        mShopingGoods.setsPrice(mGoods.getPrice());
        mShopingGoods.setToBuyNum(1);
        DBHelper.getInstance(getActivity()).addShopGoods(mShopingGoods);*/
        ShopingGoods mShopingGoods = new ShopingGoods();
        mShopingGoods.setGoods_name(mGoods.getName());
        mShopingGoods.setsPrice(mGoods.getPrice());
        mShopingGoods.setPngUrl(mGoods.getPngUrl());
        mShopingGoods.setToBuyNum(1);
        mShopingGoods.setGoodObjId(mGoods.getObjectId());
        mShopingGoods.setOwnerObjId(StaticUtil.currentUser.getObjectId());
        //DBHelper.getInstance(this).addShopGoods(mShopingGoods);
        mShopingGoods.save(getActivity(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), R.string.add_cart_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getActivity(), R.string.add_cart_fail, Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
                fragmentId = 1;
                doQuery(fragmentId);
                switchTabList(false, true, false);
                break;
            case R.id.tv_perfume_btn:
                fragmentId = 2;
                doQuery(fragmentId);
                switchTabList(false, false, true);
                break;
        }
    }

}
