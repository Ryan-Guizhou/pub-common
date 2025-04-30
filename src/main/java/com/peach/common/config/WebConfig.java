package com.peach.common.config;

import com.peach.common.request.interceptor.SqlInjectionInterceptor;
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

}
