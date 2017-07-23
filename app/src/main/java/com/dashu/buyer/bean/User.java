package com.dashu.buyer.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/4/26.
 */
public class User extends BmobUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mansex;
    private String card;
    private String phone;
    private Integer roleType;

    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    private String receiver;
    private String address;
    //
    private double money;

    //和聊天有关
    private String avatar;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMansex() {
        return mansex;
    }

    public void setMansex(String mansex) {
        this.mansex = mansex;
    }
}
