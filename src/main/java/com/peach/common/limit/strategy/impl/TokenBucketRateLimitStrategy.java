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
 * @Description 令牌桶限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class TokenBucketRateLimitStrategy extends RateLimitStrategy {

    private final double permitsPerSecond;
    private final int capacity;

    public TokenBucketRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType,
                                     double tokenRate, int bucketCapacity) {
        super(timeWindow, maxPermits, limitType);
        this.permitsPerSecond = tokenRate;      // 令牌产生速率（个/秒）
        this.capacity = bucketCapacity;         // 令牌桶容量
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.TOKEN_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1]\n" +
                "local rate = tonumber(ARGV[1])\n" +
                "local capacity = tonumber(ARGV[2])\n" +
                "local current_time = tonumber(ARGV[3])\n" +
                "local requested = tonumber(ARGV[4])\n" +
                "local tokens_count_field = 'bucket_token_count'\n" +
                "local last_refreshed_field = 'last_update_time'\n" +
                "\n" +
                "local fill_time = capacity/rate\n" +
                "local ttl = math.floor(fill_time*2)\n" +
                "\n" +
                "local last_tokens = tonumber(redis.call(\"hget\", key, tokens_count_field))\n" +
                "if last_tokens == nil then\n" +
                "  last_tokens = capacity\n" +
                "end\n" +
                "\n" +
                "local last_refreshed = tonumber(redis.call(\"hget\", key, last_refreshed_field))\n" +
                "if last_refreshed == nil then\n" +
                "  last_refreshed = 0\n" +
                "end\n" +
                "\n" +
                "local delta = math.max(0, current_time - last_refreshed)\n" +
                "local filled_tokens = math.min(capacity, math.floor(last_tokens + (delta / 1000 * rate)))\n" +
                "local allowed = filled_tokens >= requested\n" +
                "local remain_tokens = filled_tokens\n" +
                "local allowed_num = 0\n" +
                "if allowed then\n" +
                "  remain_tokens = filled_tokens - requested\n" +
                "  allowed_num = 1\n" +
                "end\n" +
                "\n" +
                "redis.call(\"hset\", key, tokens_count_field, remain_tokens)\n" +
                "redis.call(\"hset\", key, last_refreshed_field, current_time)\n" +
                "redis.call(\"expire\", key, ttl)\n" +
                "\n" +
                "return { allowed_num, remain_tokens }";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        long currentTime = System.currentTimeMillis();
        List<Long> result = (List<Long>) executeRedisLua(
                script,
                Collections.singletonList(key),
                permitsPerSecond,
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
            log.info("请求通过，剩余令牌数: {}", result.get(1));
        } else {
            log.info("请求被限流: {}", key);
        }
        return allowed;
    }
}