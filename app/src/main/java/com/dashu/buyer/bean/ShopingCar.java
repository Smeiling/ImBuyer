package com.dashu.buyer.bean;

import cn.bmob.v3.BmobObject;

public class ShopingCar extends BmobObject {
	/**
	 *
	 */
	private int _id;
	private String account;
	private String time;
	private String address;
	private float totalNum;
	private float totalPrice;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(float totalNum) {
		this.totalNum = totalNum;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}


}

