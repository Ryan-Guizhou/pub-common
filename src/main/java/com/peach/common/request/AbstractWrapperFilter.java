package com.peach.common.request;

import com.peach.common.request.wrapper.RequestWrapper;
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
 * @Description //TODO
 * @CreateTime 2025/3/16 22:45
 */
@Slf4j
public abstract class AbstractWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 这里只在需要包装请求的情况下进行包装
        RequestWrapper wrappedRequest = request instanceof RequestWrapper ? (RequestWrapper) request : new RequestWrapper(request);

        // 继续处理过滤链
        doFilter(wrappedRequest, response, filterChain);
    }


    /**
     * 由子类实现特定的过滤逻辑
     */
    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
