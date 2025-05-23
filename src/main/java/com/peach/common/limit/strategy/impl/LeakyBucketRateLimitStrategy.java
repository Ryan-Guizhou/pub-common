package com.peach.common.limit.strategy.impl;


import com.peach.common.entity.LimitType;
import com.peach.common.constant.RateTypeConstants;
import com.peach.common.limit.strategy.RateLimitStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 漏桶限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class LeakyBucketRateLimitStrategy extends RateLimitStrategy {

    private final double outflowRate;
    private final int capacity;

    public LeakyBucketRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType,
                                     double outflowRate, int bucketCapacity) {
        super(timeWindow, maxPermits, limitType);
        this.outflowRate = outflowRate;      // 漏水速率（请求/秒）
        this.capacity = bucketCapacity;         // 漏桶容量
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.LEAKY_BUCKET_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1]\n" +
                "local leak_rate = tonumber(ARGV[1])\n" +
                "local capacity = tonumber(ARGV[2])\n" +
                "local current_time = tonumber(ARGV[3])\n" +
                "local requested = tonumber(ARGV[4])\n" +
                "local water_level = 'water_level'\n" +
                "local last_leak_time = 'last_leak_time'\n" +
                "\n" +
                "local last_water = tonumber(redis.call('hget', key, water_level))\n" +
                "if last_water == nil then\n" +
                "    last_water = 0\n" +
                "end\n" +
                "\n" +
                "local last_leak = tonumber(redis.call('hget', key, last_leak_time))\n" +
                "if last_leak == nil then\n" +
                "    last_leak = 0\n" +
                "end\n" +
                "\n" +
                "local delta_time = math.max(0, current_time - last_leak)\n" +
                "local leaked = math.min(last_water, (delta_time / 1000) * leak_rate)\n" +
                "local current_water = math.max(0, last_water - leaked)\n" +
                "\n" +
                "local allowed = current_water + requested <= capacity\n" +
                "local allowed_num = 0\n" +
                "if allowed then\n" +
                "    current_water = current_water + requested\n" +
                "    allowed_num = 1\n" +
                "end\n" +
                "\n" +
                "redis.call('hset', key, water_level, current_water)\n" +
                "redis.call('hset', key, last_leak_time, current_time)\n" +
                "redis.call('expire', key, 3600)\n" +
                "\n" +
                "return { allowed_num, capacity - current_water }";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        long currentTime = System.currentTimeMillis();
        List<Long> result = (List<Long>) executeRedisLua(
                script,
                Collections.singletonList(key),
                outflowRate,
                capacity,
                currentTime,
                1
        );

        if (result == null) {
            log.error("Redis执行异常");
            return false;
        }

        boolean allowed = result.get(0) == 1;
        if (allowed) {
            log.info("请求通过，剩余容量: {}", result.get(1));
        } else {
            log.info("请求被限流: {}", key);
        }
        return allowed;
    }
}