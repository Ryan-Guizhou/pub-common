package com.peach.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 状态枚举类
 * @CreateTime 2025/2/26 14:20
 */
public enum StatusEnum {

    SUCCESS("200", "操作成功"),
    FAIL("500", "操作失败"),
    PARAM_ERROR("400", "参数错误"),
    NOT_FOUND("404", "资源找不到");

    private final String code;

    private final String msg;

    private static final Map<String, StatusEnum> ENUM_MAP = new ConcurrentHashMap<>();

    static {
        Arrays.stream(StatusEnum.values()).forEach(statusEnum -> {
            ENUM_MAP.put(statusEnum.code, statusEnum);
        });
    }

    StatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public static StatusEnum getStatusEnum(int code) {
        return ENUM_MAP.getOrDefault(code, null);
    }

}
