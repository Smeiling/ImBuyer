package com.dashu.buyer.bean;

public class Address {
	
	private int _id;//id
	private String account;//�˺�
	private String address;//��ַ
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String toString(){
		return address;
	}

}
