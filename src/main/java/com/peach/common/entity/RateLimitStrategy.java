package com.peach.common.entity;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流策略枚举类
 * @CreateTime 2025/5/18 19:44
 */
public enum RateLimitStrategy {
        FIXED_WINDOW("FIXED_WINDOW","固定窗口限流"),
        SLIDING_WINDOW("SLIDING_WINDOW","滑动窗口限流"),
        TOKEN_BUCKET("TOKEN_BUCKET","令牌桶限流"),
        LEAKY_BUCKET("LEAKY_BUCKET","漏桶限流"),
        COUNTER("COUNTER","计数器限流"),
        CONCURRENT("CONCURRENT","并发限流");

        private final String code;

        private final String desc;

        RateLimitStrategy(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }