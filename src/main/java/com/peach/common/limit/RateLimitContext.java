package com.peach.common.limit;

import com.peach.common.anno.RateLimit;
import com.peach.common.entity.LimitType;
import com.peach.common.limit.factory.RateLimitStrategyFactory;
import com.peach.common.limit.strategy.RateLimitStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流上下文 , 项目启动时初始化需要限流的方法
 * @CreateTime 2025/4/30 20:06
 */
@Slf4j
public class RateLimitContext implements ApplicationContextAware {

    public static Map<Integer, RateLimitStrategy> RATE_LIMIT_STRATEGY_MAP = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansMap = applicationContext.getBeansWithAnnotation(RateLimit.class);
        if (beansMap.isEmpty()) {
            log.warn("当前项目没有找到rest接口");
            return;
        }

        beansMap.forEach((beanName, bean) -> {
            Class<?> targetClass = bean.getClass();
            Method[] methods = targetClass.getSuperclass().getMethods();
            if (targetClass.isAnnotationPresent(RateLimit.class)) {
                RateLimit rateLimit = targetClass.getAnnotation(RateLimit.class);
                RateLimitStrategy rateLimitStrategy = getRateLimitStrategy(rateLimit);
                for (Method method : methods) {
                    if(rateLimitStrategy != null){
                        RATE_LIMIT_STRATEGY_MAP.put(method.hashCode(), rateLimitStrategy);
                    }
                }
            }
            for (Method method : methods) {
                RateLimit rateLimit = targetClass.getAnnotation(RateLimit.class);
                RateLimitStrategy rateLimitStrategy = getRateLimitStrategy(rateLimit);
                if(rateLimitStrategy != null){
                    RATE_LIMIT_STRATEGY_MAP.put(method.hashCode(), rateLimitStrategy);
                }
            }
        });
        log.info("RateLimitContext => 初始化限流方法完成，限流接口总数为:[{}]",RATE_LIMIT_STRATEGY_MAP.size());
    }

    /**
     * 创建限流器
     * @param rateLimit
     * @return
     */
    private RateLimitStrategy getRateLimitStrategy(RateLimit rateLimit) {
        int maxPermits = rateLimit.maxPermits();
        int timeWindow = rateLimit.timeWindow();
        String strategy = rateLimit.strategy().getCode();
        LimitType limitType = rateLimit.limitType();
        RateLimitConfig rateLimitConfig = new RateLimitConfig();
        rateLimitConfig.setTimeWindow(timeWindow);
        rateLimitConfig.setMaxPermits(maxPermits);
        rateLimitConfig.setLimitType(limitType);
        rateLimitConfig.setStrategy(strategy);
        RateLimitStrategy rateLimitStrategy = RateLimitStrategyFactory.createStrategy(rateLimitConfig);
        return rateLimitStrategy;
    }

}
