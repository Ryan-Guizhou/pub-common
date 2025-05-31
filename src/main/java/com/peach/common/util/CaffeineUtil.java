package com.peach.common.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description // caffine 缓存工具类
 * @CreateTime 2024/10/14 15:51
 */
public class CaffeineUtil {

    /**
     * 内部维护多个命名缓存池（按 cacheName 分组）
     */
    private static final Map<String, Cache<Object, Object>> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取或创建缓存池
     * @param cacheName 缓存名称
     * @return
     */
    private static Cache<Object, Object> getOrCreateCache(String cacheName) {
        return CACHE_MAP.computeIfAbsent(cacheName, name ->
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(10, TimeUnit.SECONDS)
                        .build());
    }

    /**
     * 无参封装（适合只有一个对象需要缓存的场景）
     * @param cacheName 缓存名称
     * @param supplier 回调函数
     * @return
     * @param <V>
     */
    public static <V> V get(String cacheName, Supplier<V> supplier) {
        Object key = "__default__";
        return get(cacheName, key, k -> supplier.get());
    }

    /**
     * 有参封装（根据 key 缓存不同对象）
     * @param cacheName 缓存名称
     * @param key  缓存key
     * @param mappingFunction 回调函数
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> V get(String cacheName, K key, Function<K, V> mappingFunction) {
        Cache<Object, Object> cache = getOrCreateCache(cacheName);
        return (V) cache.get(key, k -> mappingFunction.apply((K) k));
    }

    /**
     * 主动删除缓存
     * @param cacheName 缓存名称
     * @param key 缓存key
     */
    public static void clear(String cacheName, Object key) {
        Cache<Object, Object> cache = CACHE_MAP.get(cacheName);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    /**
     * 删除整个缓存池
     * @param cacheName 缓存名称
     */
    public static void clearAll(String cacheName) {
        Cache<Object, Object> cache = CACHE_MAP.get(cacheName);
        if (cache != null) {
            cache.invalidateAll();
        }
    }
}
