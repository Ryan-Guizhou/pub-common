package com.peach.common.log.aspect;

import com.peach.common.IRedisDao;
import com.peach.common.anno.UserOperLog;
import com.peach.common.log.TransferUtil;
import com.peach.common.log.LogQueue;
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
 * @Description //TODO
 * @CreateTime 2025/3/14 18:07
 */
@Slf4j
public class UserOperLogInteceptor implements MethodInterceptor {

    private final IRedisDao redisDao = InstanceLazyLoader.getInstance(IRedisDao.class);

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Method method = invocation.getMethod();
        UserOperLog operLog = method.getAnnotation(UserOperLog.class);
        if (operLog == null) {
            return invocation.proceed();
        }
        Object[] arguments = invocation.getArguments();
        Object proceed = invocation.proceed();
        Response response = (Response) proceed;
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        Map<String, Object> userOperLog = TransferUtil.transferToOperLog(invocation, operLog, totalTime, response);
        LogQueue.getInstance().addOperLogQueue(userOperLog);
        return proceed;
    }

}
