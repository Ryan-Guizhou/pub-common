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
 * @Description 固定窗口限流策略
 * @CreateTime 2025/5/18 19:44
 */
@Slf4j
public class FixedWindowRateLimitStrategy extends RateLimitStrategy {

    public FixedWindowRateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType) {
        super(timeWindow, maxPermits, limitType);
    }

    @Override
    protected String getCompleteKey(String key) {
        return String.format("%s_%s_%s", RateTypeConstants.FIXED_WINDOW_LIMIT, limitType.name(), key);
    }

    @Override
    protected String getLuaScript() {
        return "local key = KEYS[1] -- 限流资源\n" +
                "local maxPermits = ARGV[1] -- 最大许可数\n" +
                "local timeWindow = ARGV[2] -- 时间窗口大小（秒）\n" +
                "local currentCount = redis.call('get', key) -- 当前请求数\n" +
                "if (currentCount and tonumber(currentCount) >= tonumber(maxPermits)) then\n" +
                "    return 0\n" +
                "end\n" +
                "redis.call('incr', key)\n" +
                "redis.call('expire', key, timeWindow)\n" +
                "return tonumber(maxPermits) - tonumber(currentCount)";
    }

    @Override
    protected boolean doAcquire(String key, String script) {
        List<Long> re = (List<Long>) executeRedisLua(script, Arrays.asList(key), maxPermits, timeWindow);
        Long result = re.get(0);
        if (result != null && result == -1) {
            log.info("请求被限流: {}", key);
            return false;
        }
        log.info("请求通过，剩余可用请求数: {}", result);
        return true;
    }
}