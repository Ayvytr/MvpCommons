package com.base.utils;

/**
 * Created by xiesuichao on 2017/7/24.
 */

import android.text.TextUtils;

import com.ayvytr.logger.L;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ArithUtil {

    private static final String APPROXIMATE_TRILLION = "999999999999.99";
    private static final String APPROXIMATE_BILLION = "9999999999.99";
    private static final String TRILLION = "1000000000000";
    private static final String BILLION = "1000000000";

    private ArithUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 除法的商默认10位小数
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 四舍五入最低小位数取值
     */
    private static final int MIN_SCALE = 0;

    /**
     * 默认结果为0
     */
    private static final String DEF_RESULT = "0";

    private static final BigDecimal COIN_SHOW_MAX_VALUE = new BigDecimal("99999999999999");

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.add(b2).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static String sub(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.subtract(b2).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.multiply(b2).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 提供（相对）精确的除法运算
     * 小数点以后scale位，以后的数字四舍五入。
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2, int scale) {
        if (scale < MIN_SCALE) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_DOWN).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static String div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static String div(String v1, String v2, int scale) {
        if (scale < MIN_SCALE) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round(String v, int scale) {
        if (scale < MIN_SCALE) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        try {
            BigDecimal b = new BigDecimal(v);
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 判断是不是自然数
     *
     * @param number 需要判断的数字
     * @return true:自然数
     */
    public static boolean isNatural(String number) {
        return new BigDecimal(number).compareTo(new BigDecimal("0")) >= 0;
    }

    /**
     * 截取有多位小数的数字
     *
     * @param v     需要截取的数字
     * @param scale 小数点后保留几位
     * @return 截取后的结果
     */
    public static String roundDown(String v, int scale) {
        if (scale < MIN_SCALE) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        try {
            BigDecimal b = new BigDecimal(v);
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_DOWN).toPlainString();
        } catch (Exception e) {
            return DEF_RESULT;
        }
    }

    /**
     * 比较两个数的大小
     *
     * @param v1
     * @param v2
     * @return true 前一个数值大 false 后一个数值大
     */
    public static boolean bigVol(String v1, String v2) {
        if (TextUtils.isEmpty(v1) || TextUtils.isEmpty(v2)) {
            return false;
        }

        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            if (b1.compareTo(b2) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            L.e(e.getMessage());
            return false;
        }

    }


    /**
     * 货币格式化
     * 整数+小数点+小数位数 最多展示14个字符
     *
     * @param val
     * @return
     */
    public static String formatCoin(String val) {
        String result;
        int maxLength = 14;
        int maxScale = 8;
        try {
            BigDecimal bigDecimalVal = new BigDecimal(val).setScale(maxScale, BigDecimal.ROUND_DOWN);
            String str = bigDecimalVal.toPlainString();
            if (bigDecimalVal != null && bigDecimalVal.subtract(COIN_SHOW_MAX_VALUE).longValue() > 1) {
                // 整数位大于14位
                return str.substring(0, maxLength).concat("..");
            } else if (str != null && str.length() > maxLength) {
                // 整数位小于14位，总长度大于14位
                result = str.substring(0, maxLength);
            } else {
                // 总长度小于14位
                result = str;
            }
        } catch (Exception e) {
            L.e(e.getMessage());
            result = "";
        }
        return result;
    }

    /**
     * @param coinNumber 需要转换的数据
     * @param scale      保留多少位小数
     * @return 转换后的数据
     */
    public static String coinNumberFormat(String coinNumber, int scale) {
        try {
            if (bigVol(coinNumber, APPROXIMATE_TRILLION)) {
                coinNumber = String.format("%s trillion", div(coinNumber, TRILLION, scale));
            } else if (bigVol(coinNumber, APPROXIMATE_BILLION)) {
                coinNumber = String.format("%s billion", div(coinNumber, BILLION, scale));
            } else {
                coinNumber = roundDown(coinNumber, scale);
            }
        } catch (Exception e) {
            L.e(e.getMessage());
            coinNumber = "0";
        }

        return coinNumber;
    }
}
