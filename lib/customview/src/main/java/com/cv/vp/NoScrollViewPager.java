package com.cv.vp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <br> ClassName: NoScrollViewPager
 * <br> Description: 禁用滑动的ViewPager
 * <p>
 * <br> Author: hemin
 * <br> Date: 2018/4/19 18:54
 */
public class NoScrollViewPager extends ViewPagerFixed {

    //默认不可以滑动
    private boolean isCanScroll = false;

    private boolean isSmoothScroll = false; // 默认不带动画滑动

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否可以滑动
     *
     * @param isCanScroll
     */
    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        isSmoothScroll = smoothScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isSmoothScroll);
    }
}
