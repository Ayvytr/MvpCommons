package com.common.biz.setting;

import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import com.common.R;
import com.common.base.BaseApplication;

import java.util.Locale;

/**
 * Created by Jason on 2018/11/13.
 * <p>
 * 语言设置管理
 */

public class LanguageManager {

    //简体中文
    public static final String ZH_CN = "zh_cn";
    //繁体中文
    public static final String ZH_TW = "zh_tw";
    public static final String EN_US = "en_us";
    public static final String MS_MY = "ms_my";
    public static final String KO_KR = "ko_kr";
    public static final String KM_KH = "km_kh";
    public static final String JA_JP = "ja_jp";
    private String mLanguage;

    private String mLanguageFlag;

    private static LanguageManager mInstance;

    private LanguageManager() {
//        mLanguageFlag = new SpCache(BaseApplication.getInstance()).getString(IntentConstant.EXTRA_LANGUAGE_FLAG);
//        mLanguage = new SpCache(BaseApplication.getInstance()).getString(IntentConstant.EXTRA_LANGUAGE);
    }


    public static LanguageManager getInstance() {
        if (mInstance == null) {
            mInstance = new LanguageManager();
        }
        return mInstance;
    }

    public void saveLanguageFlag(String languageFlag) {
        if (!TextUtils.isEmpty(languageFlag)) {
//            new SpCache(BaseApplication.getInstance()).setData(IntentConstant.EXTRA_LANGUAGE_FLAG, languageFlag);
            this.mLanguageFlag = languageFlag;
        }
    }

//    public String getLanguageFlag() {
//        if (TextUtils.isEmpty(mLanguageFlag)) {
//            return BaseApplication.getInstance().getString(R.string.common_default_language_flag);
//        } else {
//            return mLanguageFlag;
//
//        }
//    }

    public boolean isCn() {
        return getLanguageFlag().equals("zh_cn");
    }

    /**
     * 获取语言设置标志位
     *
     * @return
     */
   public String getLanguageFlag() {
        if (TextUtils.isEmpty(mLanguageFlag)) {
            String sysLocale = getSysLocale();
            if ("zh_CN".equals(sysLocale) || "zh_".startsWith(sysLocale)) {
                return "zh_cn";
            } else if ("zh_TW".equals(sysLocale)) {
                return "zh_tw";
            } else if ("en_US".equals(sysLocale) || "en_".startsWith(sysLocale)) {
                return "en_us";
            } else if ("ms_MY".equals(sysLocale)) {
                return "ms_my";
            } else if ("ko_KR".equals(sysLocale) || "ko_".startsWith(sysLocale)) {
                return "ko_kr";
            } else if ("km_KH".equals(sysLocale)) {
                return "km_kh";
            } else if ("ja_JP".equals(sysLocale) || "ja_".startsWith(sysLocale)) {
                return "ja_jp";
            } else {
                return sysLocale.toLowerCase();
            }
        } else {
            return mLanguageFlag;
        }
    }

    public void setLanguage(String language) {
        if (!TextUtils.isEmpty(language)) {
//            new SpCache(BaseApplication.getInstance()).setData(IntentConstant.EXTRA_LANGUAGE, language);
            this.mLanguage = language;
        }
    }


    public String getLanguage() {
        if (TextUtils.isEmpty(mLanguage)) {
            return BaseApplication.getInstance().getString(R.string.common_default_language);
        } else {
            return mLanguage;
        }
    }

    /**
     * 获取用户设置语言
     * @return
     */
   /* public String getLanguage() {
        if (TextUtils.isEmpty(mLanguage)) {
            String sysLocale = getSysLocale();
            if ("zh_CN".equals(sysLocale)) {
                return "简体中文";
            } else if ("zh_TW".equals(sysLocale)) {
                return "繁體中文";
            } else if ("en_US".equals(sysLocale)) {
                return "English";
            } else if ("ms_MY".equals(sysLocale)) {
                return "Melayu";
            } else if ("ko_KR".equals(sysLocale)) {
                return "한국어";
            } else if ("km_KH".equals(sysLocale)) {
                return "កម្ពុជា";
            } else {
                return "English";
            }
        } else {
            return mLanguage;
        }
    }*/

    /**
     * 获取用户系统语言配置
     *
     * @return
     */
    public String getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
//            LogUtil.d("http:" + locale.getLanguage() + "_"+ locale.getCountry());
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage() + "_" + locale.getCountry();
    }
}
