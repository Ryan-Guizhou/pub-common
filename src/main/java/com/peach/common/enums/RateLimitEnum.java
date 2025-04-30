package com.peach.common.enums;

import com.peach.common.constant.PubCommonConst;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/4/30 15:08
 */
public enum RateLimitEnum {
    /**
     * 达到并发数直接执行决绝策略
     */
    REFUSE_LIMIT {
        @Override
        public String val() {
            return PubCommonConst.REFUSE;
        }
    },
    /**
     * 平滑限流
     */
    SMOOTH_LIMIT {
        @Override
        public String val() {
            return PubCommonConst.SMOOTH;
        }
    };

    public String val() {
        throw new AbstractMethodError();
    }
}
