package com.peach.common.cache;

import com.peach.common.anno.cache.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAspect {

    @Autowired
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