package com.common.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * @author Administrator
 */
public class AccountUtil {
    public static boolean isPhone(@NonNull String account) {
        if(TextUtils.isDigitsOnly(account)) {
            return true;
        }

        return false;
    }
}
