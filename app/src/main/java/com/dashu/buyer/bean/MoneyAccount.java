package com.dashu.buyer.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/12/26.
 */
public class MoneyAccount extends BmobObject {
    public final static int FLAG_INCOME = 1;
    public final static int FLAG_OUTCOME = 0;
    private String userId;
    private double money;// 存储支出金额
    private String time;// 存储支出时间
    private String type;// 存储支出类别
    private String address;// 存储支出地点
    private String mark;// 存储支出备注
    private int inOrOut;//支出账户 0 ，还是收入账户 1

    public MoneyAccount(String userid, double money, String time, String type,
                        String address, String mark,int inOrOut) {
        super();
        setUserId(userid);
        setMoney(money);
        setTime(time);
        setType(type);
        setAddress(address);
        setMark(mark);
        setInOrOut(inOrOut);
    }



    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }
}
