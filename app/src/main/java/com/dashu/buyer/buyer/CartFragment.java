package com.dashu.buyer.buyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.bean.CartBean;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.R;
import com.dashu.buyer.adapter.CartItemAdapter;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.util.PerferenceUtil;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.view.ChoiceView;
import com.dashu.buyer.view.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

import static com.dashu.buyer.R.mipmap.cart;

/**
 * Created by Administrator on 2017/4/25.
 */
public class CartFragment extends Fragment {

    private ListView mListView;
    public static List<ShopingGoods> mList = null;
    private Button buyButton;
    private DBHelper mDbHelper;
    private TextView totalNumTextView;
    private TextView totalPriceTextView;
    private int totalNum;
    private float totalPrice;
    private String address;
    private String account;
    private CartItemAdapter myAdapter;

    private List<CartBean> queryList = new ArrayList<>();

    private List<ShopingGoods> tmpList = new ArrayList<>();
    private int[] titlePos;

    private List<Map<CartBean, List<ShopingGoods>>> totalList = new ArrayList<>();
    private List<CartBean> cartList = new ArrayList<>();
    private CustomProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_shop_hall, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    private void initView(View view) {
        address = PerferenceUtil.getInstance(getActivity()).getString(StaticUtil.address);
        account = PerferenceUtil.getInstance(getActivity()).getString(StaticUtil.LOGIN_ACCOUNT);
        mDbHelper = DBHelper.getInstance(getActivity());
        mListView = (ListView) view.findViewById(R.id.listView_tobuy);
        totalNumTextView = (TextView) view.findViewById(R.id.textView_total_num);
        totalPriceTextView = (TextView) view.findViewById(R.id.textView_total_price);
        //doQuery();
        buyButton = (Button) view.findViewById(R.id.button_buy);

        buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                toBuy();

            }
        });
    }

    public void showDeleteDialog(final ShopingGoods mGoods, final int position) {
        final ShopingGoods name = mGoods;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除");
        builder.setMessage("确认从购物车里删除？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.w(StaticUtil.TAG, "delete");
                        deleteFromCart(mGoods, position);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void deleteFromCart(ShopingGoods mGoods, final int position) {
        mGoods.delete(getActivity(), new DeleteListener() {
            @Override
            public void onSuccess() {
                Log.d("TAG", "delete success");
                tmpList.remove(position);
                for (int i = 0; i < titlePos.length; i++) {
                    if (position - 1 == titlePos[i]) {
                        if (((i + 1) < titlePos.length) && ((position + 1) == titlePos[i + 1])) {
                            tmpList.remove(position - 1);
                        } else if (position == tmpList.size() - 1) {
                            tmpList.remove(position - 1);
                        }
                    }
                }
                myAdapter.update(tmpList);
                updateTotal(tmpList);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("TAG", "delete fail " + i + s);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume");
        if (mListView != null) {
            mListView.setAdapter(null);
        }
        doQuery();
    }

    public void clearCart() {
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        for (ShopingGoods good : tmpList) {
            good.delete(getActivity(), new DeleteListener() {
                @Override
                public void onSuccess() {
                    Log.d("TAG", "deleted");
                    if (mListView != null) {
                        mListView.setAdapter(null);
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("TAG", "delete fail " + i + s);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            });
        }
        updateTotal(null);
    }

    private void doQuery() {
        tmpList.clear();
        cartList.clear();
        queryList.clear();
        totalList.clear();
        Log.w(StaticUtil.TAG, "doQuery");
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        BmobQuery<CartBean> query = new BmobQuery<>();
        query.addWhereEqualTo("buyerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(getActivity(), new FindListener<CartBean>() {
            @Override
            public void onSuccess(List<CartBean> list) {
                Log.w(StaticUtil.TAG, "cart num =" + list.size());
                searchGoodsByCarts(list);
            }

            @Override
            public void onError(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });

    }

    private void searchGoodsByCarts(List<CartBean> list) {
        cartList = list;
        queryList = list;
        getTotalGoods(totalList);
    }

    private void getTotalGoods(final List<Map<CartBean, List<ShopingGoods>>> totalList) {
        Log.w(StaticUtil.TAG, "totalListSize " + totalList.size());
        Log.w(StaticUtil.TAG, "queryListSize " + queryList.size());
        if (queryList.size() == 0) {
            //TODO:show to listview
            fillCart(totalList);
        } else {
            Log.w(StaticUtil.TAG, "search goods ");
            BmobQuery<ShopingGoods> query = new BmobQuery<>();
            final CartBean cart = queryList.remove(0);
            query.addWhereEqualTo("cartObjId", cart.getObjectId());
            query.findObjects(getActivity(), new FindListener<ShopingGoods>() {
                @Override
                public void onSuccess(List<ShopingGoods> list) {
                    if (list.size() > 0) {
                        Map<CartBean, List<ShopingGoods>> map = new HashMap<CartBean, List<ShopingGoods>>();
                        map.put(cart, list);
                        totalList.add(map);
                        Log.w(StaticUtil.TAG, "totalSize " + totalList.size());
                        Log.w(StaticUtil.TAG, "search goods onSuccess");
                    }
                    getTotalGoods(totalList);
                }

                @Override
                public void onError(int i, String s) {
                    Log.w(StaticUtil.TAG, "search goods onError");
                }
            });
        }
    }

    private void fillCart(List<Map<CartBean, List<ShopingGoods>>> list) {
        Log.w(StaticUtil.TAG, "totalList" + list.size());
        tmpList = new ArrayList<>();
        titlePos = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ShopingGoods fake = new ShopingGoods();
            Iterator iter = list.get(i).entrySet().iterator();
            Object key = null;
            Object val = null;
            if (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key = entry.getKey();
                val = entry.getValue();
            }
            fake.setGoods_name(((CartBean) key).getSellerName());
            tmpList.add(fake);
            titlePos[i] = tmpList.size() - 1;
            tmpList.addAll(list.get(i).get((CartBean) key));
        }

        myAdapter = new CartItemAdapter(getActivity(), tmpList);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (list.size() > 0) {
            mListView.setAdapter(myAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i : titlePos) {
                    if (position == i) {
                        return;
                    }
                }
                BmobQuery<Goods> query = new BmobQuery();
                query.addWhereEqualTo("objectId", tmpList.get(position).getGoodObjId());
                query.findObjects(getActivity(), new FindListener<Goods>() {
                    @Override
                    public void onSuccess(List<Goods> list) {
                        Intent mIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                        mIntent.putExtra("data", list.get(0));
                        startActivity(mIntent);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                Log.w(StaticUtil.TAG, "on long click" + position);
                for (int i : titlePos) {
                    if (position == i) {
                        return true;
                    }
                }
                showDeleteDialog(tmpList.get(position), position);
                return true;
            }
        });

        updateTotal(tmpList);
    }

    private void updateTotal(List<ShopingGoods> tmpList) {
        totalNum = 0;
        totalPrice = 0;
        if (null != tmpList && tmpList.size() > 0) {
            for (ShopingGoods mGoods : tmpList) {
                if (null != mGoods.getOwnerObjId()) {
                    totalNum += mGoods.getToBuyNum();
                    totalPrice += mGoods.getsPrice() * mGoods.getToBuyNum();
                }
            }
        }
        totalNumTextView.setText(String.format("总量: %d", totalNum));
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String price = decimalFormat.format(totalPrice);//format 返回的是字符串
        price = price.equals(".00") ? "0" : price;
        totalPriceTextView.setText("总价: " + price + " 元");
        if (tmpList == null || tmpList.size() < 1) {
            buyButton.setClickable(false);
        } else {
            buyButton.setClickable(true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        Log.d("TAG", "onHiddenChanged" + hidden);
        if (!hidden) {
            totalNum = 0;
            totalPrice = 0;
            //mList = mDbHelper.getAllShopingGoods();
            if (tmpList != null && tmpList.size() > 0) {
                for (ShopingGoods mGoods : tmpList) {
                    if (mGoods.getOwnerObjId() != null) {
                        totalNum += mGoods.getToBuyNum();
                        totalPrice += mGoods.getsPrice() * mGoods.getToBuyNum();
                    }
                }
            }
            totalNumTextView.setText(String.format("总数:%d", totalNum));
            totalPriceTextView.setText(String.format("总价:%f", totalPrice));

            mListView.setAdapter(new CartItemAdapter(getActivity(), tmpList));
        }

    }

    private void toBuy() {
        Intent mIntent = new Intent(getActivity(), BookActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("totalNum", totalNum);
        mBundle.putFloat("totalPrice", totalPrice);
        //mBundle.putString("imgUrl", tmpList.get(0).getPngUrl());
        mBundle.putSerializable("orderName", (Serializable) totalList);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            mListView.setAdapter(null);
            updateTotal(null);
        }
    }
}
