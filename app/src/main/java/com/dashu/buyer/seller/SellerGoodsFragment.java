package com.dashu.buyer.seller;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dashu.buyer.GoodsFlowActivity;
import com.dashu.buyer.adapter.OrderAdapter;
import com.dashu.buyer.buyer.GoodsDetailActivity;
import com.dashu.buyer.seller.AddGoodsActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.adapter.GoodsAdapter;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.seller.GoodsEditActivity;
import com.dashu.buyer.util.DataUtils;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;


/**
 * ���ﳵ
 *
 * @author 123
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SellerGoodsFragment extends Fragment implements View.OnClickListener {

    private int fragmentId = 0;

    private String account;
    private String objId;


    //tabs
    private ListView lvCosList;
    private TextView cosTab;
    private TextView skTab;
    private TextView perfumeTab;
    private View cosLine;
    private View skLine;
    private View perfumeLine;

    private List<Goods> cosList;
    private List<Goods> resultList;
    private CustomProgressDialog progressDialog;
    Map<Integer, Integer> goodCount = new HashMap<Integer, Integer>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = StaticUtil.currentUser.getUsername();
        objId = StaticUtil.currentUser.getObjectId();
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_goods_seller, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());

    }


    @Override
    public void onResume() {
        super.onResume();
        getGoodCounts();
        doQuery(fragmentId);
    }

    private void setTitle(Map<Integer, Integer> map) {
        if (map.size() < 1) {
            //TODO:set all count 0 when no search result
            cosTab.setText("彩妆" + " (0)");
            skTab.setText("护肤品" + " (0)");
            perfumeTab.setText("香水" + " (0)");
        } else {
            if (map.get(0) != null && map.get(0) > 0) {
                cosTab.setText("彩妆" + " (" + map.get(0) + ")");
            } else {
                cosTab.setText("彩妆" + " (0)");
            }
            if (map.get(1) != null && map.get(1) > 0) {
                skTab.setText("护肤品" + " (" + map.get(1) + ")");
            } else {
                skTab.setText("护肤品" + " (0)");
            }
            if (map.get(2) != null && map.get(2) > 0) {
                perfumeTab.setText("香水" + " (" + map.get(2) + ")");
            } else {
                perfumeTab.setText("香水" + " (0)");
            }
        }
    }

    private void initView(View view) {

        lvCosList = (ListView) view.findViewById(R.id.listview_cos);
        lvCosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                mIntent.putExtra("data", cosList.get(position));
                startActivity(mIntent);
            }
        });

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.addgood, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddGoodsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void doQuery(int type) {

        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        if (null != lvCosList) {
            lvCosList.setAdapter(null);
        }
        BmobQuery<Goods> query = new BmobQuery<Goods>();

        query.addWhereEqualTo("sellerObjectId", StaticUtil.currentUser.getObjectId());
        query.addWhereEqualTo("type", type);

        query.findObjects(getActivity(), new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    fillList(object);
                } else {
                    fillList(object);
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

    private void fillList(List<Goods> object) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (object.size() < 1) {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.tv_no_result).setVisibility(View.GONE);
        }

        resultList = object;
        cosList = object;
        GoodsAdapter gAdapter = new GoodsAdapter(getActivity(), object);
        lvCosList.setAdapter(gAdapter);

    }

    private void getGoodCounts() {
        Log.w(StaticUtil.TAG, "getGoodCount");
        BmobQuery<Goods> query = new BmobQuery<>();
        query.groupby(new String[]{"type"});
        query.addWhereEqualTo("sellerObjectId", StaticUtil.currentUser.getObjectId());
        query.setHasGroupCount(true);
        query.findStatistics(getActivity(), Goods.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    try {
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = ary.getJSONObject(i);
                            String type = obj.getString("type");
                            int count = obj.getInt("_count");//setHasGroupCount设置为true时，返回的结果中含有"_count"字段
                            Log.w("TAG", "总共统计了"
                                    + count + "条记录,type = " + type);
                            goodCount.put(Integer.parseInt(type), count);
                        }
                        setTitle(goodCount);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.w("TAG", "查询成功，无数据");
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void showFunctionDialog(final Goods goods) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(getResources().getStringArray(R.array.user_function_seller), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //收藏
                    doCollectionGoods(goods);
                } else if (which == 1) {
                    //加入购物车
                    doAddShopCar(goods);
                }

            }
        }).create();
        builder.show();


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
        //Toast.makeText(getActivity(), "add " + mGoods.getName(), Toast.LENGTH_LONG).show();
        ShopingGoods mShopingGoods = new ShopingGoods();
        mShopingGoods.setGoods_name(mGoods.getName());
        mShopingGoods.setsPrice(mGoods.getPrice());
        mShopingGoods.setToBuyNum(1);
        DBHelper.getInstance(getActivity()).addShopGoods(mShopingGoods);

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
