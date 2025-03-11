package com.peach.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description FIFO缓存实现
 * @CreateTime 2024/10/12 18:35
 */
@Slf4j
@Indexed
@Service
//@ConditionalOnProperty(prefix = "localCache.enableCache",name = "type" ,havingValue = "FIFO" ,matchIfMissing = false)
public class FIFOCache<K, V> extends AbstractCacheMap<K, V> {


    /**
     * 构造访求
     *
     * @param cacheSize
     * @param defaultExpire
     */
    public FIFOCache(@Value("${localCache.cacheSize:5}") int cacheSize, @Value("${localCache.defaultExpire:3000}") long defaultExpire) {
        super(cacheSize, defaultExpire);
        cacheMap = new LinkedHashMap<K, CacheObject<K, V>>(cacheSize + 1, 1F, false);
    }

    @Override
    protected int eliminateCache() {

        int count = 0;
        K firstKey = null;

        Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            CacheObject<K, V> cacheObject = iterator.next();

            if (cacheObject.isExpired()) {
                iterator.remove();
                count++;
            } else {
                if (firstKey == null) {
                    firstKey = cacheObject.key;
                }
            }
        }

        //删除过期对象还是满,继续删除链表第一个
        if (firstKey != null && isFull()) {
            cacheMap.remove(firstKey);
        }

        return count;
    }

}
