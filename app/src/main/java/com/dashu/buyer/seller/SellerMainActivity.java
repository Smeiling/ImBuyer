package com.dashu.buyer.seller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dashu.buyer.R;

import java.util.ArrayList;

import com.dashu.buyer.ShopCarActivity;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.buyer.SearchActivity;
import com.dashu.buyer.buyer.UserMainActivity;
import com.dashu.buyer.frg.ConversationFragment;
import com.dashu.buyer.util.Logger;
import com.dashu.buyer.util.StaticUtil;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class SellerMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    Fragment goodsFrg;
    Fragment chatFrg;
    Fragment meFragment;
    private String account;
    private String objId;

    private ImageView ivCart;
    private ImageView ivSearch;

    private ArrayList<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        account = StaticUtil.currentUser.getUsername();
        objId = StaticUtil.currentUser.getObjectId();
        initTitle();
        initValues();
        connect();

    }

    private void initTitle() {
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        ivCart.setImageResource(R.mipmap.add);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddGoodsActivity.class);
                startActivity(intent);
            }
        });

        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(StaticUtil.TAG, "ivSearch onClicked");
                Intent mIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void initValues() {


        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.icon_goods, R.string.item_goods).setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.icon_chat, R.string.item_chats).setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.icon_mine, R.string.item_me).setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.layFrame, goodsFrg);
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        //
        goodsFrg = new SellerGoodsFragment();
        chatFrg = new ConversationFragment();
        meFragment = new SellerMeFragment();

        fragments.add(goodsFrg);
        fragments.add(chatFrg);
        fragments.add(meFragment);

        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        resetTitleBar(position);
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.show(fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }

    }


    private void resetTitleBar(int position) {
        switch (position) {
            case 0:
                findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
                ivCart.setVisibility(View.VISIBLE);
                break;
            case 1:
                findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.INVISIBLE);
                ivCart.setVisibility(View.INVISIBLE);
                break;
            case 2:
                findViewById(R.id.ll_title).setVisibility(View.GONE);
                ivSearch.setVisibility(View.INVISIBLE);
                ivCart.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    private void connect() {
        User user = BmobUser.getCurrentUser(this, User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出");
            builder.setMessage("确认退出代你美 ？");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
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
        return super.onKeyDown(keyCode, event);
    }
}
