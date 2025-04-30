package com.peach.common.log.aspect;

import com.peach.common.IRedisDao;
import com.peach.common.log.LogQueue;
import com.peach.common.log.TransferUtil;
import com.peach.common.response.Response;
import com.peach.common.util.InstanceLazyLoader;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 登录日志拦截器
 * @CreateTime 2025/3/14 18:07
 */
@Slf4j
public class LoginLogInteceptor implements MethodInterceptor {

    private final IRedisDao redisDao = InstanceLazyLoader.getInstance(IRedisDao.class);

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();

        Object proceed = invocation.proceed();
        Response response = (Response) proceed;
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        Map<String, Object> loginLog = TransferUtil.transferToLoginLog(invocation, totalTime, response);
        LogQueue.getInstance().addLoginLogQueue(loginLog);
        return proceed;
    }



}
