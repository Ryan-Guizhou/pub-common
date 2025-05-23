package com.peach.common.limit.strategy.impl;


import com.peach.common.entity.LimitType;
import com.peach.common.constant.RateTypeConstants;
import com.peach.common.limit.strategy.RateLimitStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 并发量控制限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class ConcurrentRateLimitStrategy extends RateLimitStrategy {

    public ConcurrentRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType) {
        super(timeWindow, maxPermits, limitType);
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.CONCURRENT_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1]\n" +
                "local max_concurrent = tonumber(ARGV[1])\n" +
                "local current_time = tonumber(ARGV[2])\n" +
                "local timeout = tonumber(ARGV[3])\n" +
                "\n" +
                "-- 清理过期的并发记录\n" +
                "redis.call('zremrangebyscore', key, 0, current_time - timeout)\n" +
                "\n" +
                "-- 获取当前并发数\n" +
                "local current_concurrent = redis.call('zcard', key)\n" +
                "\n" +
                "if current_concurrent >= max_concurrent then\n" +
                "    return {0, 0}\n" +
                "end\n" +
                "\n" +
                "-- 添加新的并发记录\n" +
                "redis.call('zadd', key, current_time, current_time .. ':' .. math.random())\n" +
                "redis.call('expire', key, timeout)\n" +
                "\n" +
                "return {1, max_concurrent - current_concurrent - 1}";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        long currentTime = System.currentTimeMillis();
        List<Long> result = (List<Long>) executeRedisLua(
                script,
                Arrays.asList(key),
                maxPermits,    // 最大并发请求数
                currentTime,    // 当前时间戳
                timeWindow     // 并发请求的超时时间
        );

        if (result == null) {
            log.error("Redis执行异常");
            return false;
        }

        boolean allowed = result.get(0) == 1;
        if (allowed) {
            log.info("请求通过，剩余可用并发数: {}", result.get(1));
        } else {
            log.info("请求被限流: {}", key);
        }
        return allowed;
    }
}