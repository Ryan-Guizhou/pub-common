package com.peach.common.cache;

import com.peach.common.anno.cache.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@ConditionalOnProperty(name = "localCache.enableCache", matchIfMissing = true,havingValue = "true")
public class CacheAspect {

    @Resource
    private CacheMap cacheMap;

    @Around("@annotation(cacheable)")
    public Object cacheMethod(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String key = cacheable.key();
        Object cachedValue = cacheMap.get(key);
        
        if (cachedValue != null) {
            return cachedValue; // 返回缓存的值
        }
        
        Object result = joinPoint.proceed(); // 执行原方法
        cacheMap.put(key, result); // 将结果放入缓存
        return result;
    }
} 