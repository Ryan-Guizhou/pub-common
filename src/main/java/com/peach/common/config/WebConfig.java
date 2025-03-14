package com.peach.common.config;

import com.peach.common.anno.UserOperLog;
import com.peach.common.filter.CostTimeFiler;
import com.peach.common.log.aspect.LoginInteceptor;
import com.peach.common.log.aspect.UserOperLogInteceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 18:03
 */
@Configuration
public class WebConfig {

    /**
     * 过滤器注册
     * @return
     */
    @Bean
    public CostTimeFiler costTimeFiler() {
        return new CostTimeFiler();
    }

    // #############################################

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
