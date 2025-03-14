package com.peach.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description LRU缓存实现
 * @CreateTime 2024/10/12 18:35
 */
@Slf4j
@Indexed
@Service
@ConditionalOnProperty(prefix = "localCache.enableCache",name = "type" ,havingValue = "LRU" ,matchIfMissing = false)
public class LRUCache<K, V> extends AbstractCacheMap<K, V> {

    /**
     * 构造方法
     *
     * @param cacheSize
     * @param defaultExpire
     */
    public LRUCache(@Value("${localCache.cacheSize}") int cacheSize, @Value("${localCache.defaultExpire}") long defaultExpire) {

        super(cacheSize, defaultExpire);

        //linkedHash已经实现LRU算法 是通过双向链表来实现，
        // 当某个位置被命中，通过调整链表的指向将该位置调整到头位置，
        // 新加入的内容直接放在链表头，如此一来，最近被命中的内容就向链表头移动，
        // 需要替换时，链表最后的位置就是最近最少使用的位置
        this.cacheMap = new LinkedHashMap<K, CacheObject<K, V>>(cacheSize + 1, 1f, true) {

			private static final long serialVersionUID = 4789439462791125917L;

			@Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {

                return LRUCache.this.removeEldestEntry(eldest);
            }

        };
    }

    /**
     * 插入元素时是否需要移除最老的元素(超过缓存大小时)
     *
     * @param eldest
     * @return
     */
    private boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {

        if (cacheSize == 0)
            return false;

        return size() > cacheSize;
    }

    /**
     * 只需要实现清除过期对象就可以了,linkedHashMap已经实现LRU
     */
    @Override
    protected int eliminateCache() {

        if (!isNeedClearExpiredObject()) {
            return 0;
        }

        Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            CacheObject<K, V> cacheObject = iterator.next();

            if (cacheObject.isExpired()) {
                iterator.remove();
                count++;
            }
        }

        return count;
    }

}
