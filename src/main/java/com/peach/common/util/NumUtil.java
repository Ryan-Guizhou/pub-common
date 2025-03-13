package com.peach.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description // 数字工具类
 * @CreateTime 2025/3/13 12:23
 */
@Slf4j
public class NumUtil {

    private static final Random RANDOM = new Random();

    /**
     * 生成指定范围内的随机整数
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * 判断一个字符串是否是纯数字
     * @param character
     * @return
     */
    public static boolean isNumeric(String character) {
        return StringUtil.isNotBlank(character) && character.matches("\\d+");
    }



    /**
     * 计算幂
     * @param base
     * @param exponent
     * @return
     */
    public static long power(int base, int exponent) {
        return (long) Math.pow(base, exponent);
    }

}
