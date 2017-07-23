package com.dashu.buyer.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/29.
 */
public class Frezz extends BmobObject{
    private User user;
    private String userId;
    private boolean isFrezz;


    public boolean isFrezz() {
        return isFrezz;
    }

    public void setIsFrezz(boolean isFrezz) {
        this.isFrezz = isFrezz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
