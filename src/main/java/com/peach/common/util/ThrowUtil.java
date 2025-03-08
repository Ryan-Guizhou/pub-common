package com.peach.common.util;

import com.peach.common.enums.StatusEnum;
import com.peach.common.exception.BusniessException;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 17:42
 */
public class ThrowUtil {

    /**
     *
     * 如果条件成立，抛出异常
     * @param condition 条件
     * @param exception 异常
     */
    public static void throwIf(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

    /**
     *
     * 如果条件成立，抛出异常
     * @param condition 条件
     * @param statusEnum 错误码
     * @param message 异常信息
     */
    public static void throwIf(boolean condition, StatusEnum statusEnum, String message) {
        throwIf(condition,new BusniessException(statusEnum,message));
    }

    /**
     *
     * 如果条件成立，抛出异常
     * @param condition 条件
     * @param statusEnum 错误码
     */
    public static void throwIf(boolean condition, StatusEnum statusEnum) {
        throwIf(condition,new BusniessException(statusEnum));
    }
}
