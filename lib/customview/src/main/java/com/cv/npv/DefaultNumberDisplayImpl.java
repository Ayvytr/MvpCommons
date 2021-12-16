package com.cv.npv;

import android.text.TextPaint;

/**
 * @author Administrator
 */
public class DefaultNumberDisplayImpl extends NumberPickerView.Display {
    public DefaultNumberDisplayImpl() {
//        Log.e("tag", DefaultNumberDisplayImpl.class.getSimpleName());
    }

    @Override
    protected CharSequence getDisplayValue(int i, int minValue, int maxValue) {
//        L.e(maxValue);
        return String.valueOf(minValue + i);
    }

    @Override
    public int getRealValueCount(int mMinValue, int mMaxValue) {
        if(mMinValue == mMaxValue) {
            return 1;
        }
        return mMaxValue - mMinValue + 1;
    }

    @Override
    public int getMaxTextWidth(TextPaint mPaintText, int mMinValue, int mMaxValue) {
        return (int) mPaintText.measureText(mMaxValue + "");
    }
}
