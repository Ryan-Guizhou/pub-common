package com.peach.common.config;

import com.peach.common.anno.HandleLogin;
import com.peach.common.anno.RateLimit;
import com.peach.common.anno.UserOperLog;
import com.peach.common.limit.RateLimitInterceptor;
import com.peach.common.log.aspect.LoginLogInteceptor;
import com.peach.common.log.aspect.UserOperLogInteceptor;
import com.peach.common.request.interceptor.NoRepeatSubmitInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 全局拦截器注册
 * @CreateTime 2025/4/30 20:00
 */
@Component
public class InterceptorConfig {


    /**
     * 用户操作日志拦截器注册
     * @return
     */
    @Bean
    public Advisor userOperLogHandlerAdvisor() {
        UserOperLogInteceptor userOperLogInteceptor = new UserOperLogInteceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, UserOperLog.class);
        return buildAdvisor(userOperLogInteceptor,pointcut);
    }

    /**
     * 用户登录日志拦截器注册
     * @return
     */
    @Bean
    public Advisor loginLogHandlerAdvisor() {
        LoginLogInteceptor loginLogInteceptor = new LoginLogInteceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, HandleLogin.class);
        return buildAdvisor(loginLogInteceptor, pointcut);
    }


    /**
     * 限流拦截器
     * @return
     */
    @Bean
    public Advisor rateLimitHandlerAdvisor() {
        RateLimitInterceptor rateLimitInterceptor = new RateLimitInterceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, RateLimit.class);
        return buildAdvisor(rateLimitInterceptor, pointcut);
    }

    /**
     * 重复请求拦截器
     * @return
     */
    @Bean
    public Advisor noRepeatSubmitHandlerAdvisor() {
        NoRepeatSubmitInterceptor noRepeatSubmitInterceptor = new NoRepeatSubmitInterceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, RateLimit.class);
        return buildAdvisor(noRepeatSubmitInterceptor, pointcut);
    }
    /**
     * 构建拦截器
     * @param advice 环绕通知
     * @param pointcut 切点
     * @return
     */
    private Advisor buildAdvisor(Advice advice, Pointcut pointcut){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(advice);
        advisor.setPointcut(pointcut);
        return advisor;
    }

}
