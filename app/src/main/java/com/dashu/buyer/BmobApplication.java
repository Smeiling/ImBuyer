package com.dashu.buyer;

import android.app.Application;

import com.dashu.buyer.im.DemoMessageHandler;
import com.dashu.buyer.im.base.UniversalImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;


/**
 * Created by Administrator on 2016/4/26.
 */
public class BmobApplication extends Application{
    /**
     */
    public static String APPID = "0e2c3306eda5c9e272134925585220b5";

    @Override
    public void onCreate() {
        super.onCreate();
//       //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
