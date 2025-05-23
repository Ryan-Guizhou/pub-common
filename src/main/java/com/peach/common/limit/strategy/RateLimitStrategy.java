package com.peach.common.limit.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.peach.common.entity.LimitType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流策略抽象
 * @CreateTime 2025/5/18 19:44
 */
public abstract class RateLimitStrategy {

    protected RedisTemplate<String, String> redisTemplate = SpringUtil.getBean("redisTemplate");

    
    /**
     * 时间窗口大小（秒）
     * - 固定窗口/滑动窗口：统计时间窗口
     * - 令牌桶/漏桶：令牌产生或漏水的计算周期
     * - 计数器：统计周期
     * - 并发量：并发请求的超时时间
     */
    protected int timeWindow;

    /**
     * 最大许可数
     * - 固定窗口/滑动窗口：窗口内最大请求数
     * - 令牌桶：桶容量
     * - 漏桶：桶容量
     * - 计数器：周期内最大请求数
     * - 并发量：最大并发请求数
     */
    protected int maxPermits;

    /**
     * 限流类型（默认、用户、IP）
     */
    protected LimitType limitType;
    
    public RateLimitStrategy(int timeWindow, int maxPermits, LimitType limitType) {
        this.timeWindow = timeWindow;
        this.maxPermits = maxPermits;
        this.limitType = limitType;
    }
    
    /**
     * 获取完整的限流key
     * @param key 基础key
     * @return 完整的限流key
     */
    protected abstract String getCompleteKey(String key);
    
    /**
     * 获取限流脚本
     * @return Lua脚本
     */
    protected abstract String getLuaScript();
    
    /**
     * 执行限流逻辑
     * @param key 限流key
     * @param script Lua脚本
     * @return 是否允许通过
     */
    protected abstract boolean doAcquire(String key, String script);
    
    /**
     * 执行限流
     * @param baseKey 基础key
     * @return 是否允许通过
     */
    public final boolean execute(String baseKey) {
        String completeKey = getCompleteKey(baseKey);
        String luaScript = getLuaScript();
        return doAcquire(completeKey, luaScript);
    }
    
    /**
     * 执行Redis Lua脚本
     */
    protected Object executeRedisLua(String script, List<String> keys, Object... args) {
        return redisTemplate.execute(
            new DefaultRedisScript<>(script, List.class),
            keys,
            args
        );
    }
}