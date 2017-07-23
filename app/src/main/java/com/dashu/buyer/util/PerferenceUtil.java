package com.dashu.buyer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �������shareperferece����ӣ��޸�
 * @author 123
 *
 */
public class PerferenceUtil {
	
	public static PerferenceUtil mPerferenceUtil;
	public Context context;
	public String name = "message";
	private PerferenceUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static PerferenceUtil getInstance(Context context){
		if(mPerferenceUtil==null){
			mPerferenceUtil = new PerferenceUtil(context);
		}
		
		return mPerferenceUtil;
	}

	public String getUserId(){
		return getString("userId");
	}


	public void saveUser(String user){
		putString("userId","");
	}
	//put
	public void putString(String key,String value){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void putFloat(String key,float value){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public float getFloat(String key){
		SharedPreferences mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return mPreferences.getFloat(key, 0);
	}
	
	public void putBoolean(String key,boolean value){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public void putInt(String key,int value){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	//get
	
	public boolean getBoolean(String key){
		SharedPreferences mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return mPreferences.getBoolean(key, false);
	}
	
	public String getString(String key){
		SharedPreferences mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return mPreferences.getString(key, null);
	}
	
	public int getInt(String key){
		SharedPreferences mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return mPreferences.getInt(key, 0);
	}
	
	//clear
	public void clearBoolean(String key){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();
	}
	public void clearString(String key){
		SharedPreferences.Editor editor = context.getSharedPreferences(name,Context.MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();
	}

	
}
