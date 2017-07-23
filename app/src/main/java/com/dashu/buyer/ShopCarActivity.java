package com.dashu.buyer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dashu.buyer.buyer.CartFragment;

/**
 * Created by Administrator on 2017/4/25.
 */
public class ShopCarActivity extends AppCompatActivity {
    CartFragment shopcarFrg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        shopcarFrg = new CartFragment();
        initTitle();
        setDefaultFragment();
    }

    private void initTitle() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: clear all goods in cart
                clearCart();
            }
        });
    }

    private void clearCart() {
        shopcarFrg.clearCart();
    }


    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, shopcarFrg);
        transaction.commit();
    }
}
