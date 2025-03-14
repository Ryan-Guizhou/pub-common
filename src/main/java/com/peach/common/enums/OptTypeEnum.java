package com.peach.common.enums;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 23:17
 */
public enum OptTypeEnum {

    DELETE("DELETE","删除"),

    SELECT("SELECT","查询"),

    INSERT("INSERT","新增"),

    UPDATE("UPDATE","更新");

    /**
     * 操作类型
     */
    private final String optTypeCode;

    /**
     * 操作名称
     */
    private final String optTypeName;

    OptTypeEnum(String optTypeCode, String optTypeName) {
        this.optTypeCode = optTypeCode;
        this.optTypeName = optTypeName;
    }

    public String getOptTypeCode() {
        return optTypeCode;
    }

    public String getOptTypeName() {
        return optTypeName;
    }

}
