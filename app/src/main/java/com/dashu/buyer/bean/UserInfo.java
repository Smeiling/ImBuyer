package com.dashu.buyer.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Smeiling on 2017/5/13.
 */

public class UserInfo extends BmobObject implements Serializable {
    private String userObjId;
    private String username;
    private int roleType;
    private String phone;
    private String nick;
    private String name;
    private String address;
    private String idCard;
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
