package com.dashu.buyer.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Smeiling on 2017/5/13.
 */

public class Comment extends BmobObject implements Serializable {
    private String ownerObjId;
    private String time;
    private String content;
    private int status;

    public String getOwnerObjId() {
        return ownerObjId;
    }

    public void setOwnerObjId(String ownerObjId) {
        this.ownerObjId = ownerObjId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
