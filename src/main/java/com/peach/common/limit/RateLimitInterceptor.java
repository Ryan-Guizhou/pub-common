package com.peach.common.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.peach.common.anno.RateLimit;
import com.peach.common.enums.RateLimitEnum;
import com.peach.common.enums.SemaphoreMapKey;
import com.peach.common.response.Response;
import com.peach.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
        Map<String, Object> configMap = RateLimitContext.SEMAPHORE_MAP.get(method.hashCode());
        if (configMap != null) {
            String limitType = StringUtil.EMPTY;
            if (method.isAnnotationPresent(RateLimit.class)) {
                limitType = method.getAnnotation(RateLimit.class).LimitType().toUpperCase();
                log.info("限流类型为:[{}]",limitType);
            }else {
                limitType = method.getDeclaringClass().getAnnotation(RateLimit.class).LimitType();
                log.info("限流类型为:[{}]",limitType);
            }

            if (StringUtil.isNotBlank(limitType) && RateLimitEnum.REFUSE_LIMIT.val().equals(limitType)){
                RateLimiter limiter = getRateLimiter(method.hashCode(), configMap, limitType);
                if (!limiter.tryAcquire(1)) {
                    Response response = Response.fail();
                    log.info("当前服务繁忙,请稍后再试");
                    response.setMsg("请求过于频繁，请稍后重试");
                    return response;
                }
            }

            if (StringUtil.isNotBlank(limitType) && RateLimitEnum.SMOOTH_LIMIT.val().equals(limitType)){
                //平滑限流
                RateLimiter limiter = getRateLimiter(method.hashCode(), configMap, limitType);
                limiter.acquire(1);
            }
        }
        return null;
    }

    /**
     * 根据限流类型及配置获取限流器
     * @param methodHash 方法的hash值
     * @param configMap 限流配置
     * @param limitType 限流类型
     * @return
     */
    private RateLimiter getRateLimiter(int methodHash, Map<String, Object> configMap, String limitType) {
        RateLimiter rateLimiter = RATE_LIMITER_MAP.get(methodHash);
        if (Objects.isNull(rateLimiter)) {
            synchronized (this){
                if (Objects.isNull(rateLimiter)) {
                    // 创建一个限流器, 参数代表每条生成的令牌数
                    if (RateLimitEnum.REFUSE_LIMIT.val().equals(limitType)) {
                        rateLimiter = RateLimiter.create((Double) configMap.get(SemaphoreMapKey.PER_SECOND.name()));
                    } else {
                        rateLimiter = RateLimiter.create((Double) configMap.get(SemaphoreMapKey.PER_SECOND.name()), (Long) configMap.get(SemaphoreMapKey.WARMUP_PERIOD.name()), TimeUnit.MILLISECONDS);
                    }
                    RATE_LIMITER_MAP.put(methodHash, rateLimiter);
                }
            }
        }
        return rateLimiter;
    }
}
