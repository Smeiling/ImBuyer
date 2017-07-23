package com.dashu.buyer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dashu.buyer.bean.User;
import com.dashu.buyer.bean.UserInfo;

import java.security.acl.Owner;
import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/1/16.
 */
public class StaticUtil {

    private static final String SP_ACCOUNT_INFO = "account_info";
    private static final String CUR_ACCOUNT = "account_name";
    private static final String CUR_PASSWORD = "account_pwd";

    private static Context mContext;

    public StaticUtil(Context context) {
        mContext = context;
    }

    public static final String TAG = "DAINIMEI";

    public final static int ROLE_USER = 0;
    public final static int ROLE_MANAGER = 1;
    public final static int ROLE_SHOPPER = 2;


    public final static int Kemu2 = 20;
    public final static int Kemu3 = 40;

    public static User currentUser = null;

    public final static String LOGIN_ACCOUNT = "login_account";
    public final static String LOGIN_PASS = "login_pass";
    public final static String NOFIRST = "nofirst";
    public final static String address = "address";

    /**
     * Remember current user when new account login
     *
     * @param user
     */
    public static void setCurrentUser(Context mContext, User user, String pwd) {
        currentUser = user;
        SharedPreferences sp = mContext.getSharedPreferences(SP_ACCOUNT_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CUR_ACCOUNT, currentUser.getUsername());
        editor.putString(CUR_PASSWORD, pwd);
        editor.commit();
        updateInfo(mContext);
    }

    private static void updateInfo(final Context mContext) {
        BmobQuery<UserInfo> userinfo = new BmobQuery<>();
        userinfo.addWhereEqualTo("userObjId", currentUser.getObjectId());
        userinfo.findObjects(mContext, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                if (list.size() < 1) {
                    UserInfo info = new UserInfo();
                    info.setUserObjId(currentUser.getObjectId());
                    info.setPhone(currentUser.getUsername());
                    info.setUsername(currentUser.getUsername());
                    info.setRoleType(currentUser.getRoleType());
                    info.setIdCard(currentUser.getCard() == null ? "" : currentUser.getCard());
                    info.setNick(currentUser.getNick());
                    info.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.w(TAG, "new user info success");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.w(TAG, "new user info fail");
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.w(TAG, "fail");
            }
        });
    }

    /**
     * Auto login when sp user exists
     *
     * @return
     */
    public static String checkExistUser(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_ACCOUNT_INFO, Context.MODE_PRIVATE);
        String account = sp.getString(CUR_ACCOUNT, null);
        String password = sp.getString(CUR_PASSWORD, null);
        if (account == null || password == null) {
            return null;
        } else {
            currentUser = new User();
            currentUser.setUsername(account);
            currentUser.setPassword(password);
            return password;
        }
    }

    public static void clearSP(Context mContext) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SP_ACCOUNT_INFO, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
