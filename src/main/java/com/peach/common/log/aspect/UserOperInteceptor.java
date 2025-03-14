package com.peach.common.log.aspect;

import com.peach.common.IRedisDao;
import com.peach.common.anno.UserOperLog;
import com.peach.common.log.spel.SpelParse;
import com.peach.common.util.InstanceLazyLoader;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 18:07
 */
@Slf4j
public class UserOperInteceptor implements MethodInterceptor {

    private final IRedisDao redisDao = InstanceLazyLoader.getInstance(IRedisDao.class);

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        UserOperLog operLog = method.getAnnotation(UserOperLog.class);
        // 如果没有直接，直接跳过
        if (operLog == null) {
            return invocation.proceed();
        }
        Object[] arguments = invocation.getArguments();



        return null;
    }

    /**
     * 解析spel
     * @param objects
     * @param content
     * @return
     */
    private String getContent(Object[] objects,String content){
        try {
            if (content.contains("#p") && objects != null && objects.length > 0){
                SpelParse spelParse = SpelParse.create();
                for (int i = 0; i < objects.length; i ++) {
                    spelParse.setVariable("p" + i,objects[i]);
                }
                return spelParse.parseExpression(content);
            }
            return content;
        }catch (Exception ex){
            log.error("spel parse failed"+ex.getMessage(),e);
            return content;
        }
    }
}
