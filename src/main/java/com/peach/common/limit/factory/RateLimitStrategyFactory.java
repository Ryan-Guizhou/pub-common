package com.peach.common.limit.factory;


import com.peach.common.entity.LimitType;
import com.peach.common.limit.RateLimitConfig;
import com.peach.common.constant.RateTypeConstants;
import com.peach.common.limit.strategy.RateLimitStrategy;
import com.peach.common.limit.strategy.impl.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流工厂
 * @CreateTime 2025/5/18 19:44
 */
public class RateLimitStrategyFactory {

    private static final int DEFAULT_PERMITS_PER_SECOND = 10;

    private static final int DEFAULT_CAPACITY = 100;

    private static final double DEFAULT_LEAK_RATE = 10.0;

    public static RateLimitStrategy createStrategy(String strategy, int timeWindow, int maxPermits, LimitType limitType) {
        switch (strategy.toLowerCase()) {
            case RateTypeConstants.FIXED_WINDOW_LIMIT:
                return new FixedWindowRateLimitStrategy(timeWindow, maxPermits, limitType);
            case RateTypeConstants.SLIDING_WINDOW_LIMIT:
                return new SlideWindowRateLimitStrategy(timeWindow, maxPermits, limitType);
            case RateTypeConstants.TOKEN_LIMIT:
                return new TokenBucketRateLimitStrategy(timeWindow, maxPermits, limitType,
                        DEFAULT_PERMITS_PER_SECOND, DEFAULT_CAPACITY);
            case RateTypeConstants.LEAKY_BUCKET_LIMIT:
                return new LeakyBucketRateLimitStrategy(timeWindow, maxPermits, limitType,
                        DEFAULT_LEAK_RATE, DEFAULT_CAPACITY);
            case RateTypeConstants.COUNTER_LIMIT:
                return new CounterRateLimitStrategy(timeWindow, maxPermits, limitType);
            case RateTypeConstants.CONCURRENT_LIMIT:
                return new ConcurrentRateLimitStrategy(timeWindow, maxPermits, limitType);
            default:
                throw new IllegalArgumentException("当前限流策略不支持: " + strategy);
        }
    }


    /**
     * 通过配置创建限流器
     * @param config
     * @return
     */
    public static RateLimitStrategy createStrategy(RateLimitConfig config) {
        Integer timeWindow = config.getTimeWindow();
        int maxPermits = config.getMaxPermits();
        LimitType limitType = config.getLimitType();
        String strategy = config.getStrategy();
        return createStrategy(strategy, timeWindow, maxPermits, limitType);
    }

}