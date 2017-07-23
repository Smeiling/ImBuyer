package com.dashu.buyer.buyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.GoodsAdapter;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.view.CustomProgressDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private Spinner sSearchType;
    private EditText etSearchText;
    private TextView tvCancel;
    private ListView lvSearchResult;

    private List<Goods> goodsList;
    private CustomProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        initView();
    }

    private void initView() {
        sSearchType = (Spinner) findViewById(R.id.s_search_type);
        etSearchText = (EditText) findViewById(R.id.et_search);
        lvSearchResult = (ListView) findViewById(R.id.lv_search_result);
        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //TODO: search for result
                    if (etSearchText.getText() != null && !etSearchText.getText().toString().isEmpty()) {
                        doSearch();
                    }
                }
                return false;
            }
        });
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getApplicationContext(), GoodsDetailActivity.class);
                mIntent.putExtra("data", goodsList.get(position));
                startActivity(mIntent);
            }
        });
    }

    private void doSearch() {
        findViewById(R.id.tv_no_result).setVisibility(View.GONE);
        if (lvSearchResult != null) {
            lvSearchResult.setAdapter(null);
        }
        String searchText = etSearchText.getText().toString();
        int searchType = sSearchType.getSelectedItemPosition();
        progressDialog = CustomProgressDialog.createDialog(mContext);
        progressDialog.show();
        if (searchType == 0) {
            searchByGoods(searchText);
        } else if (searchType == 1) {
            searchBySellers(searchText);
        }
    }

    private void searchBySellers(String searchText) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("sellerName", searchText);
        query.findObjects(this, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                showSearchResults(list);
            }

            @Override
            public void onError(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Log.w(TAG, "error:" + i + s);
            }
        });

    }

    private void searchByGoods(String searchText) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("name", searchText);
        query.findObjects(this, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                showSearchResults(list);
            }

            @Override
            public void onError(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Log.w(TAG, "error:" + i + s);
            }
        });

    }

    private void showSearchResults(List<Goods> results) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (results.size()<1){
            findViewById(R.id.tv_no_result).setVisibility(View.VISIBLE);
        }
        goodsList = results;
        GoodsAdapter goodsAdapter = new GoodsAdapter(this, results);
        lvSearchResult.setAdapter(goodsAdapter);
    }

}
