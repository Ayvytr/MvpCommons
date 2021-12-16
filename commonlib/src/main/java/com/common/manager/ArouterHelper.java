package com.common.manager;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author Administrator
 */
public class ArouterHelper {
    public static void navigate(final String path, Context context) {
        ARouter.getInstance().build(path)
               .navigation(context);
    }

    public static Postcard build(final String path) {
        return ARouter.getInstance().build(path);
    }
}
