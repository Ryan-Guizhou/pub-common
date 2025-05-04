package com.peach.common.config;

import com.peach.common.request.interceptor.SqlInjectionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    /**
     * 注册过滤器 并配置拦截那些路由
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sqlInjectionInterceptor)
                .addPathPatterns("/**")  // 你可以指定需要拦截的路径
                .excludePathPatterns("/swagger-resources/**", "/v2/api-docs");  // 可以排除不需要拦截的路径
    }

    /**
     * 配置跨域请求、请求方式等
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

}
