package com.dashu.buyer.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @author tanglong
 */

public class ShopingGoods extends BmobObject implements Serializable {

    //商品名称
    private String goods_name;//��
    //购买数量
    private int toBuyNum;//��
    //单价
    private float sPrice;//�
    //图片
    private String pngUrl;

    private String goodObjId;
    private String ownerObjId;
    private String sellerObjId;
    private String cartObjId;
    private String orderObjId;
    private int goodState;//0 to buy, 1 buy done


    public String getCartObjId() {
        return cartObjId;
    }

    public void setCartObjId(String cartObjId) {
        this.cartObjId = cartObjId;
    }

    public String getOrderObjId() {
        return orderObjId;
    }

    public void setOrderObjId(String orderObjId) {
        this.orderObjId = orderObjId;
    }

    public int getGoodState() {
        return goodState;
    }

    public void setGoodState(int goodState) {
        this.goodState = goodState;
    }

    public String getGoodObjId() {
        return goodObjId;
    }

    public void setGoodObjId(String goodObjId) {
        this.goodObjId = goodObjId;
    }

    public String getSellerObjId() {
        return sellerObjId;
    }

    public void setSellerObjId(String sellerObjId) {
        this.sellerObjId = sellerObjId;
    }

    public String getOwnerObjId() {
        return ownerObjId;
    }

    public void setOwnerObjId(String ownerObjId) {
        this.ownerObjId = ownerObjId;
    }


    public int getToBuyNum() {
        return toBuyNum;
    }

    public void setToBuyNum(int toBuyNum) {
        this.toBuyNum = toBuyNum;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public float getsPrice() {
        return sPrice;
    }

    public void setsPrice(float sPrice) {
        this.sPrice = sPrice;
    }


    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }
}
