package com.dashu.buyer.view;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.CartItemAdapter;
import com.dashu.buyer.bean.CartBean;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.util.StaticUtil;

import java.util.List;

/**
 * Created by Smeiling on 2017/5/5.
 */

public class ChoiceView extends LinearLayout implements Checkable {
    ExpandedListView lvCartGoods;
    TextView rbSheller;
    ImageView ivCheckBox;

    private Context mContext;

    public ChoiceView(Context context) {
        super(context);
        mContext = context;
        View.inflate(context, R.layout.cart_item, this);
        lvCartGoods = (ExpandedListView) findViewById(R.id.lv_cart_goods);
        rbSheller = (TextView) findViewById(R.id.seller_rb);
        ivCheckBox = (ImageView) findViewById(R.id.iv_checkbox);
        ivCheckBox.setVisibility(GONE);
        lvCartGoods.setClickable(false);
    }

    public void setContent(CartBean cart, List<ShopingGoods> goods) {
        CartItemAdapter myAdapter = new CartItemAdapter(mContext, goods);
        lvCartGoods.setAdapter(myAdapter);
        rbSheller.setText(cart.getSellerName());
    }

    @Override
    public void setChecked(boolean checked) {
        Log.w(StaticUtil.TAG, "setChecked" + checked);
        if (checked) {
            ivCheckBox.setVisibility(VISIBLE);
        } else {
            ivCheckBox.setVisibility(GONE);
        }
    }

    @Override
    public boolean isChecked() {
        Log.w(StaticUtil.TAG, "isChecked");
        return ivCheckBox.getVisibility() == VISIBLE;
    }

    @Override
    public void toggle() {
        Log.w("ChoiceView", "toggle()");
    }

}