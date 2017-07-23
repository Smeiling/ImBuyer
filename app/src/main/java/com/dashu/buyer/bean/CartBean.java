package com.dashu.buyer.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Smeiling on 2017/5/4.
 */

public class CartBean extends BmobObject implements Serializable {
    private String sellerObjId;
    private String buyerObjId;
    private String sellerName;
    private int cartState;

    public String getSellerObjId() {
        return sellerObjId;
    }

    public void setSellerObjId(String sellerObjId) {
        this.sellerObjId = sellerObjId;
    }

    public String getBuyerObjId() {
        return buyerObjId;
    }

    public void setBuyerObjId(String buyerObjId) {
        this.buyerObjId = buyerObjId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getCartState() {
        return cartState;
    }

    public void setCartState(int cartState) {
        this.cartState = cartState;
    }
}
