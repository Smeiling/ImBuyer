package com.dashu.buyer.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ToastUtils {
    public static void showTip(Context mcontext,String msg){
       // Toast.makeText(mcontext,msg,Toast.LENGTH_LONG).show();
        Log.w("ToastUtils",msg);
    }

    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy/MM/dd");
        return sdf.format(new Date());
    }

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyyMMdd");
        return sdf.format(new Date());
    }
}
