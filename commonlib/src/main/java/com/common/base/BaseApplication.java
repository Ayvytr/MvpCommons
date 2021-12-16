package com.common.base;

import android.app.Application;

/**
 * Application基类
 * </br>
 * Date: 2018/7/24 11:31
 *
 * @author hemin
 */
public class BaseApplication extends Application {
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    /**
     * 初始化
     */
    protected void init(){

    }

    public void initApiClient(){

    }

    public static Application getInstance() {
        return mInstance;
    }

}
