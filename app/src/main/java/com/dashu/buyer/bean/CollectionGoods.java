package com.dashu.buyer.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 *
 */
public class CollectionGoods extends BmobObject implements Serializable {
    private String name;
    private float price;
    private String type;
    private String brand;
    private String pngUrl;
    private String goodObjId;

    private String ownerObjId;
    public String getOwnerObjId() {
        return ownerObjId;
    }

    public void setOwnerObjId(String ownerObjId) {
        this.ownerObjId = ownerObjId;
    }

    public String getGoodObjId() {
        return goodObjId;
    }

    public void setGoodObjId(String goodObjId) {
        this.goodObjId = goodObjId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }
}
