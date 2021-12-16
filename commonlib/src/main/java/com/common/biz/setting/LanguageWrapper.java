package com.common.biz.setting;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Jason on 2018/11/20.
 *
 * 语言设置处理帮助类
 */
public class LanguageWrapper {

    private LanguageWrapper() {
    }

    public static void updateLanguage(Context context) {
        Locale targetLocale = getLanguageLocale();
        Resources resources = context.getResources();
        if (resources == null) {
            return;
        }
        Configuration configuration = resources.getConfiguration();
        if (configuration == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }

        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    private static Locale getLanguageLocale() {
        String languageFlag = LanguageManager.getInstance().getLanguageFlag();
        Locale targetLocale = Locale.SIMPLIFIED_CHINESE;
        switch (languageFlag) {
            case "en_us":
                targetLocale = Locale.US;
                break;
            case "zh_cn":
                targetLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "zh_tw":
                targetLocale = Locale.TRADITIONAL_CHINESE;
                break;
            case "ms_my":
                targetLocale = new Locale("ms", "MY");
                break;
            case "ko_kr":
                targetLocale = Locale.KOREA;
                break;
            case "ja_jp":
                targetLocale = Locale.JAPAN;
                break;
            case "km_kh":
                targetLocale = new Locale("km", "KH");
                break;
            case "system_default":
                targetLocale = getSysLocale();
        }

        return targetLocale;
    }

    //以上获取方式需要特殊处理一下
    private static Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }


    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            updateLanguage(context);
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {

        Resources resources = context.getResources();
        if (resources == null) {
            return context;
        }
        Configuration configuration = resources.getConfiguration();
        if (configuration == null) {
            return context;
        }
        Locale locale = getLanguageLocale();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, null);
//        return context.createConfigurationContext(configuration);
        return context;
    }
}
