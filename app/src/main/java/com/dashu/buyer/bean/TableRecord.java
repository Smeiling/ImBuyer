package com.dashu.buyer.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/11.
 */
public class TableRecord extends BmobObject{
    private String houseName;
    private String houseNumber;
    private String rentMan;
    private long waterNow;
    private long waterLast;
    private long eNow;
    private long eLast;
    private double waterPrice;
    private double ePrice;


    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }


    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRentMan() {
        return rentMan;
    }

    public void setRentMan(String rentMan) {
        this.rentMan = rentMan;
    }

    public long getWaterNow() {
        return waterNow;
    }

    public void setWaterNow(long waterNow) {
        this.waterNow = waterNow;
    }

    public long getWaterLast() {
        return waterLast;
    }

    public void setWaterLast(long waterLast) {
        this.waterLast = waterLast;
    }

    public long geteNow() {
        return eNow;
    }

    public void seteNow(long eNow) {
        this.eNow = eNow;
    }

    public long geteLast() {
        return eLast;
    }

    public void seteLast(long eLast) {
        this.eLast = eLast;
    }

    public double getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(double waterPrice) {
        this.waterPrice = waterPrice;
    }

    public double getePrice() {
        return ePrice;
    }

    public void setePrice(double ePrice) {
        this.ePrice = ePrice;
    }
}
