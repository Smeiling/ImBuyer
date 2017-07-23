package com.dashu.buyer.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.CommentAdapter;
import com.dashu.buyer.adapter.GoodsAdapter;
import com.dashu.buyer.bean.Comment;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.buyer.GoodsDetailActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class KefuActivity extends Activity implements View.OnClickListener {

    private int fragmentId = 0;
    private ImageView ivBack;
    //tabs
    private ListView lvCosList;

    private TextView cosTab;
    private TextView skTab;
    private TextView perfumeTab;
    private View cosLine;
    private View skLine;
    private View perfumeLine;
    private CustomProgressDialog progressDialog;
    private TextView tvNoResult;

    private List<Comment> curList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kefu);
        initView();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        doQuery(fragmentId);
    }

    private void initView() {
        tvNoResult = (TextView) findViewById(R.id.tv_no_result);
        lvCosList = (ListView) findViewById(R.id.lv_comment);
        lvCosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.w(StaticUtil.TAG, "position = " + position);
            }
        });

        lvCosList.setVisibility(View.VISIBLE);

        cosTab = (TextView) findViewById(R.id.tv_cos_btn);
        skTab = (TextView) findViewById(R.id.tv_sk_btn);
        perfumeTab = (TextView) findViewById(R.id.tv_perfume_btn);

        cosTab.setOnClickListener(this);
        skTab.setOnClickListener(this);
        perfumeTab.setOnClickListener(this);
        cosLine = findViewById(R.id.cos_line);
        skLine = findViewById(R.id.sk_line);
        perfumeLine = findViewById(R.id.perfume_line);
        switchTabList(true, false, false);

    }

    private void switchTabList(boolean b, boolean b1, boolean b2) {
        if (b) {
            cosLine.setVisibility(View.VISIBLE);
        } else {
            cosLine.setVisibility(View.INVISIBLE);
        }
        if (b1) {
            skLine.setVisibility(View.VISIBLE);
        } else {
            skLine.setVisibility(View.INVISIBLE);
        }
        if (b2) {
            perfumeLine.setVisibility(View.VISIBLE);
        } else {
            perfumeLine.setVisibility(View.INVISIBLE);
        }
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

    private void doQuery(int fragmentId) {
        progressDialog = CustomProgressDialog.createDialog(this);
        progressDialog.show();
        if (lvCosList != null) {
            lvCosList.setAdapter(null);
        }
        tvNoResult.setVisibility(View.GONE);
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("status", fragmentId);
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    Log.d("TAG", "object size = " + object.size());
                    fillCos(object);
                } else {
                    fillCos(object);
                    ToastUtils.showTip(getApplication(), "还没商品录入：");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                ToastUtils.showTip(getApplication(), "查询失败：" + msg);
            }
        });
    }

    private void fillCos(List<Comment> obj) {
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

        CommentAdapter gAdapter = new CommentAdapter(this, obj);
        lvCosList.setAdapter(gAdapter);
        //progressDialog.dismiss();
    }
}
