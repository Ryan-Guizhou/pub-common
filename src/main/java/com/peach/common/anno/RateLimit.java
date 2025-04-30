package com.peach.common.anno;

import com.peach.common.constant.PubCommonConst;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 限流注解,可适用于接口也可适用于类
 * @CreateTime 2025/4/30 14:34
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流类型
     * REFUSE 直接限流(每秒达到请求次数拒绝访问)
     * SMOOTH 平滑限流(将每个请求的时间间隔平均分配)
     *
     * @return
     */
    String LimitType() default PubCommonConst.REFUSE;

    /**
     * 每秒新增的令牌数
     *
     * @return
     */
    double perSecond() default 50;

    /**
     * 表示从冷启动速率过渡到平均速率的时间间隔
     * 默认为一秒
     *
     * @return
     */
    long warmupPeriod() default 1000;

}
