package com.peach.common.request;

import com.peach.common.request.wrapper.RepeatedlyRequesWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 可重复读拦截器
 * @CreateTime 2025/3/16 22:49
 */
@Slf4j
public abstract class AbstractWrapperInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            // multipart 请求不要包装，避免流提前关闭
            return handleInceptor(request, response, handler);
        } else {
            RepeatedlyRequesWrapper wrappedRequest = request instanceof RepeatedlyRequesWrapper
                    ? (RepeatedlyRequesWrapper) request
                    : new RepeatedlyRequesWrapper(request);
            return handleInceptor(wrappedRequest, response, handler);
        }
    }


    /**
     * 由子类实现特定的拦截器逻辑
     */
    protected abstract boolean handleInceptor(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}
