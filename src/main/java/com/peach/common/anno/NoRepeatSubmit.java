package com.peach.common.anno;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/4/30 19:58
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 锁的唯一值
     * @return
     */
    String uniqueKey() default "";

    /**
     * 默认的解锁时间60s
     * @return
     */
    long timeOut() default 60;
}
