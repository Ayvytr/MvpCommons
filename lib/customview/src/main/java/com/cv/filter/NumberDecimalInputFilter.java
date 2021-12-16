package com.cv.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 小数位数输入筛选器
 *
 * @author wangdunwei
 */
public class NumberDecimalInputFilter implements InputFilter {
    private int digitsLength;

    /**
     * @param digitsLength 小数位数，一定要大于0
     */
    public NumberDecimalInputFilter(int digitsLength) {
        this.digitsLength = digitsLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if("".equals(source.toString())) {
            return null;
        }

        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if(splitArray.length > 1) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - digitsLength;
            if(diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }
}
