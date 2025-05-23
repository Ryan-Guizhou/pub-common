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
 * @Description 滑动窗口限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class SlideWindowRateLimitStrategy extends RateLimitStrategy {

    public SlideWindowRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType) {
        super(timeWindow, maxPermits, limitType);
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.SLIDING_WINDOW_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1]  -- 限流关键字\n" +
                "local current_time_key = KEYS[2]   -- 当前时间戳的key\n"+
                "local window_size = tonumber(ARGV[1])  -- 滑动窗口大小\n" +
                "local limit = tonumber(ARGV[2])  -- 限制的请求数\n" +
                "local current_time = tonumber(ARGV[3])  -- 当前时间戳\n" +
                "local last_requested = 0   -- 已经用掉的请求数\n"+
                "local remain_request = 0   -- 剩余可以分配的请求数\n"+
                "local allowed_num = 0  -- 本次允许通过的请求数\n" +
                "\n" +
                "local exists_key = redis.call('exists', key)\n" +
                "if (exists_key == 1) then\n" +
                "    last_requested = redis.call('zcard', key)\n" +
                "end\n"+
                "remain_request = limit - last_requested\n" +
                "if (last_requested < limit) then\n" +
                "    allowed_num = 1\n" +
                "    redis.call('zadd', key, current_time, current_time_key)\n" +
                "end\n" +
                "redis.call('zremrangebyscore', key, 0, current_time - window_size * 1000)\n" +
                "redis.call('expire', key, window_size)\n" +
                "\n" +
                "return { allowed_num, remain_request }";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        long currentTime = System.currentTimeMillis();
        String currentTimeKey = String.valueOf(currentTime);
        List<Long> result = (List<Long>) executeRedisLua(
                script,
                Arrays.asList(key, currentTimeKey),
                timeWindow,
                maxPermits,
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