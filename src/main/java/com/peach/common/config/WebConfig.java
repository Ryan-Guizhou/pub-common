package com.peach.common.config;

import com.peach.common.anno.UserOperLog;
import com.peach.common.log.aspect.LoginInteceptor;
import com.peach.common.log.aspect.UserOperLogInteceptor;
import com.peach.common.request.interceptor.SqlInjectionInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 18:03
 */
@Indexed
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private SqlInjectionInterceptor sqlInjectionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sqlInjectionInterceptor)
                .addPathPatterns("/**")  // 你可以指定需要拦截的路径
                .excludePathPatterns("/swagger-resources/**", "/v2/api-docs");  // 可以排除不需要拦截的路径
    }

    /**
     * 拦截器注册
     * @return
     */
    @Bean
    public Advisor userOperLogHandlerAdvisor() {
        UserOperLogInteceptor userOperLogInteceptor = new UserOperLogInteceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, UserOperLog.class);
        return buildAdvisor(userOperLogInteceptor,pointcut);
    }

    @Bean
    public Advisor userOperLogPointcut() {
        LoginInteceptor loginInteceptor = new LoginInteceptor();
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        // 指定拦截login方法
        pointcut.addMethodName("login");  // 如果是login方法
        return buildAdvisor(loginInteceptor, pointcut);
    }

    private Advisor buildAdvisor(Advice advice, Pointcut pointcut){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(advice);
        advisor.setPointcut(pointcut);
        return advisor;
    }

}
