package com.peach.common.util;

import cn.hutool.extra.spring.SpringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 使用双重检查锁实现线程安全的延迟加载单例
 * @CreateTime 2025/1/9 9:29
 */
public final class InstanceLazyLoader {

    private static final Map<Class<?>, Object> INSTANCE_CACHE = new ConcurrentHashMap<>();

    private InstanceLazyLoader() {
        // 禁止实例化
    }

    /**
     * 获取单例实例（带自定义初始化逻辑）
     *
     * @param clazz    类对象
     * @param supplier 初始化逻辑
     * @param <T>      泛型类型
     * @return 单例实例
     */
    public static <T> T getInstance(Class<T> clazz, Supplier<T> supplier) {
        Object instance = INSTANCE_CACHE.get(clazz);
        if (instance == null) { // 外层检查，避免不必要的锁
            synchronized (InstanceLazyLoader.class) {
                instance = INSTANCE_CACHE.get(clazz);
                if (instance == null) { // 内层检查，确保真正初始化
                    instance = supplier.get();
                    INSTANCE_CACHE.put(clazz, instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * 获取单例实例（默认使用 SpringUtil 加载 Bean）
     * @param clazz 类对象
     * @return  单例实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, () -> SpringUtil.getBean(clazz));
    }
}
