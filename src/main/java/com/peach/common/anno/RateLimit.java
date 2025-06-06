package com.peach.common.anno;


import com.peach.common.entity.LimitType;
import com.peach.common.entity.RateLimitStrategy;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流注解
 * @CreateTime 2025/5/18 19:44
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 时间窗口大小（秒）
     * - 固定窗口/滑动窗口：统计时间窗口
     * - 令牌桶/漏桶：令牌产生或漏水的计算周期
     * - 计数器：统计周期
     * - 并发量：并发请求的超时时间
     */
    int timeWindow() default 60;

    /**
     * 最大许可数
     * - 固定窗口/滑动窗口：窗口内最大请求数
     * - 令牌桶：桶容量
     * - 漏桶：桶容量
     * - 计数器：周期内最大请求数
     * - 并发量：最大并发请求数
     */
    int maxPermits() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.USER;

    /**
     * 限流策略，支持固定窗口、滑动窗口、令牌桶
     */
    RateLimitStrategy strategy() default RateLimitStrategy.FIXED_WINDOW;
}