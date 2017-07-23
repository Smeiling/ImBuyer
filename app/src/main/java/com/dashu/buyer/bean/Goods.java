package com.dashu.buyer.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 代购的商品
 */
public class Goods extends BmobObject implements Serializable {

    public Goods(BmobFile bmobFile) {
        this.goodImg = bmobFile;

    }

    public Goods() {

    }

    private String id;
    private String name;
    private float price;
    private int type;

    private BmobFile goodImg;

    private int count;
    private String brand;
    private String pngUrl;

    //add detail
    private String weight;//含量
    private String date;//保质期
    private String address;
    private String content;

    //和聊天有关
    private String sellerObjectId;
    private String sellerName;

    public BmobFile getGoodImg() {
        return goodImg;
    }

    public void setGoodImg(BmobFile goodImg) {
        this.goodImg = goodImg;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSellerObjectId() {
        return sellerObjectId;
    }

    public void setSellerObjectId(String sellerObjectId) {
        this.sellerObjectId = sellerObjectId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
