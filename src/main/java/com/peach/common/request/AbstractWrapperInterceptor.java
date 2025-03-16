package com.peach.common.request;

import com.peach.common.request.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/16 22:49
 */
@Slf4j
public abstract class AbstractWrapperInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 这里只在需要包装请求的情况下进行包装
        RequestWrapper wrappedRequest = request instanceof RequestWrapper ? (RequestWrapper) request : new RequestWrapper(request);

        return handleInceptor(wrappedRequest, response, handler);
    }


    /**
     * 由子类实现特定的拦截器逻辑
     */
    protected abstract boolean handleInceptor(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}
