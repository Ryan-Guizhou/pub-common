package com.peach.common.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class BigDecimalUtil {

    private static final int DEFAULT_SCALE = 10; // 默认精度

    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP; // 默认四舍五入模式

    /**
     * BigDecimal 加法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    /**
     * double 加法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(double a, double b) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b));
    }

    /**
     * BigDecimal 减法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    /**
     * double 减法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal subtract(double a, double b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b));
    }

    /**
     * BigDecimal 乘法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    /**
     * double 乘法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal multiply(double a, double b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
    }

    /**
     * BigDecimal 除法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * double 除法计算 返回值为BigDecimal
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal divide(double a, double b) {
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 百分比计算 返回值为BigDecimal
     * @param value
     * @param total
     * @return
     */
    public static BigDecimal percentage(BigDecimal value, BigDecimal total) {
        return value.multiply(BigDecimal.valueOf(100)).divide(total, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 取绝对值
     * @param value
     * @return
     */
    public static BigDecimal abs(BigDecimal value) {
        return value.abs();
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.max(b);
    }

    /**
     * 取最小值
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.min(b);
    }

    /**
     * 格式化数字（保留指定位数小数，默认四舍五入）
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal format(BigDecimal value, int scale) {
        return value.setScale(scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 判断两个 BigDecimal 是否相等（精度对比）
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static boolean equals(BigDecimal a, BigDecimal b, int scale) {
        return a.setScale(scale, DEFAULT_ROUNDING_MODE).compareTo(b.setScale(scale, DEFAULT_ROUNDING_MODE)) == 0;
    }

}
