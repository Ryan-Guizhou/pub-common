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
 * @Description 计数器限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class CounterRateLimitStrategy extends RateLimitStrategy {

    public CounterRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType) {
        super(timeWindow, maxPermits, limitType);
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.CONCURRENT_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1]\n" +
                "local limit = tonumber(ARGV[1])\n" +
                "local window = tonumber(ARGV[2])\n" +
                "local current_time = tonumber(ARGV[3])\n" +
                "local counter_key = key .. ':' .. math.floor(current_time / window)\n" +
                "\n" +
                "local current = redis.call('get', counter_key)\n" +
                "if current and tonumber(current) >= limit then\n" +
                "    return {0, 0}\n" +
                "end\n" +
                "\n" +
                "current = redis.call('incr', counter_key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', counter_key, window)\n" +
                "end\n" +
                "\n" +
                "return {1, limit - tonumber(current)}";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        List<Long> result = (List<Long>) executeRedisLua(
                script,
                Arrays.asList(key),
                maxPermits,
                timeWindow,
                currentTime
        );

        if (result == null) {
            log.error("Redis执行异常");
            return false;
        }

        boolean allowed = result.get(0) == 1;
        if (allowed) {
            log.info("请求通过，剩余可用请求数: {}", result.get(1));
        } else {
            log.info("请求被限流: {}", key);
        }
        return allowed;
    }
}