package com.m2m.shgs.android;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ayvytr.logger.L;
import com.base.manager.ActivityStack;
import com.common.base.BaseApplication;
import com.common.config.AppConfig;
import com.http.ApiClient;
import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVLogLevel;

import java.util.concurrent.Executors;

/**
 * </br>
 * Date: 2018/11/29 11:10
 *
 * @author hemin
 */
public class MyApplication extends BaseApplication {

//    public static boolean isServerTrusted = false;

//    private static SSLContext sslContext = null;

    @Override
    protected void init() {
        super.init();
        //初始化bugly
        initBugly(getApplicationContext());

        AppConfig.init(MyBuildConfig.env_mode);
        //初始化友盟
//        initUM(getApplicationContext());

        ActivityStack.registerCallback(this);

        initArouter(this);
        initLogger();
        initApiClient();

        MMKV.initialize(getApplicationContext());
        MMKV.setLogLevel(AppConfig.DEBUG ? MMKVLogLevel.LevelInfo : MMKVLogLevel.LevelNone);

//        //7.0拍照问题
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());

//        LanguageWrapper.attachBaseContext(this);
//        builder.detectFileUriExposure();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public void initApiClient() {
        ApiClient.init(AppConfig.getBaseHttpUrl(), new HeaderInterceptor());
    }

    private void initLogger() {
        L.settings().showLog(AppConfig.DEBUG);
        L.settings().tag("SmartSecurity");
    }

    private void initArouter(Application context) {
        if (AppConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(context);
    }

    private void initBugly(Context context) {
        boolean isDebug = AppConfig.DEBUG;
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = DeviceUtil.getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        // 初始化Bugly
//        CrashReport.initCrashReport(context, "?", isDebug, strategy);
    }
}
