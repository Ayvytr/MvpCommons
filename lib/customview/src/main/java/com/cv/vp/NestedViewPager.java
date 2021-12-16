package com.cv.vp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * <br> ClassName:   NestedViewPager
 * <br> Description:  嵌套ViewPager,解决ViewPager嵌套ScrollView显示不全问题
 *
 * <br> Author:      hemin
 * <br> Date:        2018/3/15 17:53
 */
public class NestedViewPager extends ViewPagerFixed {

    //默认不可以滑动
    private boolean isCanScroll = false;

    public NestedViewPager(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    /**
     * 设置是否可以滑动
     * @param isCanScroll
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);

        // 必须缓存所有page，否则 getChildAt(getCurrentItem()) 可能返还null
        this.setOffscreenPageLimit(adapter.getCount());
        this.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 切换后重新测量高度 ,让ViewPager的高度为当前page的高度
                NestedViewPager.this.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewHeight = 0;

        View childView = getChildAt(getCurrentItem());
        if(childView!=null){
            childView.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            viewHeight = childView.getMeasuredHeight();
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(viewHeight, View.MeasureSpec.EXACTLY);
        }
        Log.d("leon", "onMeasure: currentItem:"+getCurrentItem()+" viewHeight:"+viewHeight+" childView:"+childView);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
}
