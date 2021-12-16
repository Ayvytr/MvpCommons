package com.cv.vp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by xiesuichao on 2018/8/22.
 */

public class ViewPagerFixed extends ViewPager {

    public ViewPagerFixed(Context var1) {
        super(var1);
    }

    public ViewPagerFixed(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        try {
            return super.onTouchEvent(var1);
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent var1) {
        try {
            return super.onInterceptTouchEvent(var1);
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

}
