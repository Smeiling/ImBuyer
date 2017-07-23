package com.dashu.buyer.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/11.
 */
public class PayRecord extends BmobObject{
    private String time;
    private int status;//0,in,1 ,out
    private String username;
    private double money;
    private int useForIndex;


    public String toString(){
        if(status==0){
            return time +" 充值 "+money+"元";
        }else{
            return getTips(useForIndex)+time+"\n" +" 消费 "+money+"元";
        }

    }

    public String getTips(int index){
        return index==0?"水费":"电费";
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    public int getUseForIndex() {
        return useForIndex;
    }

    public void setUseForIndex(int useForIndex) {
        this.useForIndex = useForIndex;
    }
}
