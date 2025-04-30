package com.peach.common.limit;

import com.google.common.collect.Maps;
import com.peach.common.anno.RateLimit;
import com.peach.common.enums.RateLimitEnum;
import com.peach.common.enums.SemaphoreMapKey;
import com.peach.common.util.StringUtil;
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

    public static Map<Integer, Map<String,Object>> SEMAPHORE_MAP = new ConcurrentHashMap<>();

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
                String limitType = rateLimit.LimitType();
                double perSecond = rateLimit.perSecond();
                long warmupPeriod = rateLimit.warmupPeriod();
                for (Method method : methods) {
                    collectRateLimiter(limitType, perSecond, warmupPeriod, method);
                }
            }

            for (Method method : methods) {
                RateLimit rateLimit = method.getAnnotation(RateLimit.class);
                String limitType = rateLimit.LimitType();
                double perSecond = rateLimit.perSecond();
                long warmupPeriod = rateLimit.warmupPeriod();
                collectRateLimiter(limitType, perSecond, warmupPeriod, method);
            }

        });
        log.info("RateLimitContext => 初始化限流方法完成，限流接口总数为:[{}]",SEMAPHORE_MAP.size());
    }

    /**
     * 搜集限流方法信息
     * @param limitType 限流类型
     * @param perSecond 每秒新增的令牌数
     * @param warmupPeriod 表示从冷启动速率过度到平均速率的时间间隔
     * @param method
     */
    private void collectRateLimiter(String limitType, double perSecond, long warmupPeriod,Method method) {
        if (StringUtil.isBlank(limitType)) {
            log.warn("限流类型为空,方法:[{}] 不需要限流",method.getName());
            return;
        }

        Map<String, Object> contianer = Maps.newHashMap();
        if (StringUtil.isNotBlank(limitType) && RateLimitEnum.REFUSE_LIMIT.val().equals(limitType)) {
            contianer.put(SemaphoreMapKey.PER_SECOND.name(), perSecond);
            SEMAPHORE_MAP.put(method.hashCode(), contianer);
            return;
        }
        if (StringUtil.isNotBlank(perSecond) && RateLimitEnum.SMOOTH_LIMIT.val().equals(limitType)) {
            contianer.put(SemaphoreMapKey.PER_SECOND.name(), perSecond);
            contianer.put(SemaphoreMapKey.WARMUP_PERIOD.name(), warmupPeriod);
            SEMAPHORE_MAP.put(method.hashCode(), contianer);
        }
    }
}
