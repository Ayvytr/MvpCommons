package com.common.config;


import com.common.BuildConfig;

/**
 * Created by Jason on 2018/7/21.
 * <p>
 * 全局常量配置
 */

public class AppConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static final int ENV_MODE_T1 = 0; // debug
//    public static final int ENV_MODE_T2 = 1; // beta
    public static final int ENV_MODE_S1 = 2; // release

    // 0 t1环境 ，1  t2环境 ，2  s1环境
    private static int env_mode;

    private static final String DEBUG_BASE_URL = "http://192.168.1.4:8000/";
    private static final String BASE_URL = "";

    public static final String DEBUG_STRIPE_API_KEY = "pk_test_51JFrL6CKjtx6uj2ipbExuLxa8IAUr5LsI2GKSO1259U21gdE5dUbSHbTFwGiolQ2qLdjSLpx7MsNJEKljyAbQvwo00Eqpa6N1O";
    public static final String STRIPE_API_KEY = null;

    public static void init(int mode) {
        env_mode = mode;
    }

    public static String getBaseHttpUrl() {
        if(env_mode == ENV_MODE_S1) {
            return BASE_URL;
        }

        return DEBUG_BASE_URL;
    }

    public static String getStripeApiKey() {
        if(env_mode == ENV_MODE_S1) {
            return STRIPE_API_KEY;
        }

        return DEBUG_STRIPE_API_KEY;
    }

    public static boolean isT1() {
        return env_mode == ENV_MODE_T1;
    }
}
