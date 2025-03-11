package com.peach.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description LFU缓存实现
 * @CreateTime 2024/10/12 18:35
 */
@Slf4j
@Indexed
@Service
@ConditionalOnProperty(prefix = "localCache.enableCache",name = "type" ,havingValue = "LFU" ,matchIfMissing = false)
public class LFUCache<K, V> extends AbstractCacheMap<K, V> {

    /**
     * 构造方法
     *
     * @param cacheSize
     * @param defaultExpire
     */
    public LFUCache(@Value("${localCache.cacheSize}") int cacheSize,@Value("${localCache.defaultExpire}") long defaultExpire) {
        super(cacheSize, defaultExpire);
        cacheMap = new HashMap<K, CacheObject<K, V>>(cacheSize + 1);
    }

    /**
     * 实现删除过期对象 和 删除访问次数最少的对象
     *
     */
    @Override
    protected int eliminateCache() {
        Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
        int count = 0;
        long minAccessCount = Long.MAX_VALUE;
        while (iterator.hasNext()) {
            CacheObject<K, V> cacheObject = iterator.next();

            if (cacheObject.isExpired()) {
                iterator.remove();
                count++;
                continue;
            } else {
                //记录下最少的访问次数
                minAccessCount = Math.min(cacheObject.accessCount, minAccessCount);
            }
        }

        if (count > 0) {
            return count;
        }

        if (minAccessCount != Long.MAX_VALUE) {

            iterator = cacheMap.values().iterator();

            while (iterator.hasNext()) {
                CacheObject<K, V> cacheObject = iterator.next();

                cacheObject.accessCount -= minAccessCount;

                if (cacheObject.accessCount <= 0) {
                    iterator.remove();
                    count++;
                }

            }

        }

        return count;
    }
}
