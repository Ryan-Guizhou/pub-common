package com.peach.common.anno.cache;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/6 19:06
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheCut {

    /**
     * 缓存key
     * @return
     */
    String key() default "";

    /**
     * 过期时间
     */
    String expire() default "";
}
