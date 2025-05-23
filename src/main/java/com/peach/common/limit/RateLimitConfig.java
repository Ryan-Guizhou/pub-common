package com.peach.common.limit;

import com.peach.common.entity.LimitType;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流策略配置，用于创建限流器
 * @CreateTime 2025/5/15 17:24
 */
@Data
public class RateLimitConfig implements Serializable {

    private static final long serialVersionUID = 2246960108932983102L;

    /**
     * 时间窗口大小（秒）
     * - 固定窗口/滑动窗口：统计时间窗口
     * - 令牌桶/漏桶：令牌产生或漏水的计算周期
     * - 计数器：统计周期
     * - 并发量：并发请求的超时时间
     */
    private Integer timeWindow;

    /**
     * 最大许可数
     * - 固定窗口/滑动窗口：窗口内最大请求数
     * - 令牌桶：桶容量
     * - 漏桶：桶容量
     * - 计数器：周期内最大请求数
     * - 并发量：最大并发请求数
     */
    private int maxPermits;

    /**
     * 限流类型
     */
    private LimitType limitType;

    /**
     * 限流策略，支持固定窗口、滑动窗口、令牌桶、漏桶、计数器、并发量限流
     */
    private String strategy;

    /**
     * 基础Key
     */
    private String baseKey;
}
