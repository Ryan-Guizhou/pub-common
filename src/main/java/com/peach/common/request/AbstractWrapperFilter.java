package com.peach.common.request;

import com.peach.common.request.wrapper.RepeatedlyRequesWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 可重复读过滤器抽象类
 * @CreateTime 2025/3/16 22:45
 */
@Slf4j
public abstract class AbstractWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            // multipart 请求不要包装，避免流提前关闭
            filterChain.doFilter(request, response);
        } else {
            RepeatedlyRequesWrapper wrappedRequest = request instanceof RepeatedlyRequesWrapper
                    ? (RepeatedlyRequesWrapper) request
                    : new RepeatedlyRequesWrapper(request);
            filterChain.doFilter(wrappedRequest, response);
        }
    }


    /**
     * 由子类实现特定的过滤逻辑
     */
    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
