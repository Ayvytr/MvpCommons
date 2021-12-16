package com.common.manager;

import com.tencent.mmkv.MMKV;

import androidx.annotation.Keep;

/**
 * @author Administrator
 */
@Keep
public class MmkvManager {
    public static final String ID_USERINFO = "id_userinfo";
    public static final String ID_APP_CONFIG = "id_app_config";

    public static final MMKV mmkvUser = MMKV.mmkvWithID(MmkvManager.ID_USERINFO);
    public static final MMKV mmkvAppConfig = MMKV.mmkvWithID(MmkvManager.ID_APP_CONFIG);

    private static final String KEY_IS_FIRST_ENTER_APP = "key_is_first_enter_app";


    public static boolean isFirstEnterApp() {
        return mmkvAppConfig.getBoolean(KEY_IS_FIRST_ENTER_APP, true);
    }

    public static void setFirstEnteredApp() {
        mmkvAppConfig.putBoolean(KEY_IS_FIRST_ENTER_APP, false);
    }
}
