package com.peach.common.util.unique;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 16:48
 */
@Slf4j
public abstract class AbstractIDGenerator implements IDGenerator {

    protected static final String REDIS_KEY_PREFIX = "ID:STRATEGY:";

    /**
     * 使用懒加载方式
     * @return
     */
    private RedissonClient getRedissonClient() {
        return SpringUtil.getBean(RedissonClient.class);
    }

    @Override
    public String generateId() {
        String uniqueKey = REDIS_KEY_PREFIX + strategy();
        RLock lock = getRedissonClient().getLock(uniqueKey);
        boolean acquired = false;
        String uniqueId = null;
        try {
            acquired = lock.tryLock(1,30, TimeUnit.SECONDS);
            if (acquired) {
                log.info("获取到锁,uniqueKey=" + uniqueKey);
                uniqueId = doGenerateId();
                log.info("获取到锁,uniqueId=" + uniqueId);
            }
        } catch (Exception e) {
            log.error("生成ID失败: {}", uniqueKey, e);
            throw new RuntimeException(e);
        }finally {
            if (acquired && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        return uniqueId;
    }

    /**
     * 子类扩展 实现自己的逻辑
     * @return
     */
    protected abstract String doGenerateId();
}
