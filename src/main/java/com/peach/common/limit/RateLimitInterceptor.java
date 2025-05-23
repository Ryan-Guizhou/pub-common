package com.peach.common.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.peach.common.anno.RateLimit;
import com.peach.common.limit.strategy.RateLimitStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流拦截器
 * @CreateTime 2025/4/30 20:24
 */
@Slf4j
public class RateLimitInterceptor implements MethodInterceptor {

    /**
     * 存储令牌桶, 防止每次都创建令牌桶
     */
    private static final Map<Integer,RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(RateLimit.class)) {
            RateLimitStrategy rateLimitStrategy = RateLimitContext.RATE_LIMIT_STRATEGY_MAP.get(method.hashCode());
            rateLimitStrategy.execute(String.valueOf(method.hashCode()));
        }
        return invocation.proceed();
    }
}
