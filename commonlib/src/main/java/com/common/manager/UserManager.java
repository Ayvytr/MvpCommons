package com.common.manager;

import android.text.TextUtils;

import com.common.constant.Const;
import com.tencent.mmkv.MMKV;

import androidx.annotation.Keep;

/**
 * Created by Richard on 2018/7/31
 * 用户信息管理类
 */
@Keep
public class UserManager {
//    private static UserManager mInstance;
    private static MMKV mmkvUser = MmkvManager.mmkvUser;

//    private UserManager() {
//        mmkvUser = MmkvManager.mmkvUser;
//    }

//    public static UserManager getInstance() {
//        if (mInstance == null) {
//            mInstance = new UserManager();
//        }
//        return mInstance;
//    }

    public static void clearUserInfo() {
        mmkvUser.clearAll();
    }

    public static void setToken(String token) {
        mmkvUser.putString(Const.KEY_TOKEN, token);
    }

    public static String getToken() {
        return mmkvUser.getString(Const.KEY_TOKEN, "");
    }

    public static void removeToken() {
        mmkvUser.remove(Const.KEY_TOKEN);
    }

    public static boolean isLogin() {
        String token = getToken();
        return !TextUtils.isEmpty(token);
    }

//    public static void setUserInfo(UserInfo userInfo) {
//        mmkvUser.encode(Const.KEY_USER_INFO, userInfo);
//    }
//
//    public static UserInfo getUserInfo() {
//        return mmkvUser.decodeParcelable(Const.KEY_USER_INFO, UserInfo.class);
//    }
//
//    public static String getUserId() {
//        UserInfo info = getUserInfo();
//        if(info != null) {
//            return info.getUserId();
//        }
//
//        return null;
//    }

}
