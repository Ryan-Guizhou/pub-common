package com.peach.common.request.interceptor;

import com.peach.common.IRedisDao;
import com.peach.common.anno.NoRepeatSubmit;
import com.peach.common.log.spel.SpelParse;
import com.peach.common.response.Response;
import com.peach.common.util.InstanceLazyLoader;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 重复请求拦截器
 * @CreateTime 2025/4/30 16:38
 */
@Slf4j
public class NoRepeatSubmitInterceptor implements MethodInterceptor {

    private IRedisDao redisDao = InstanceLazyLoader.getInstance(IRedisDao.class);

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        NoRepeatSubmit noRepeatSubmit = method.getAnnotation(NoRepeatSubmit.class);
        if (noRepeatSubmit == null) {
            return invocation.proceed();
        }
        String lockKey = noRepeatSubmit.uniqueKey();
        Long  lockTime = noRepeatSubmit.timeOut();
        Object proceed = null;
        try{
            lockKey = getLockKeyContent(invocation.getArguments(),lockKey);
            Object o = redisDao.vGet(lockKey);
            if (Objects.nonNull(o)){
                log.warn("重复请求，请稍后再试");
                return Response.businessFailResponse("重复请求,请稍后再试");
            }
            try{
                redisDao.vSet(lockKey,1,lockTime);
            } catch (Exception e) {
                log.warn("写入 Redis 锁失败，跳过重复提交拦截，错误信息：{}", e.getMessage());
            }
            proceed = invocation.proceed();
        }catch (Exception ex){
            log.error("方法:[{}], 执行发生错误，错误信息为: {}", method.getName(), ex.getMessage(), ex);
            return proceed;
        }finally{
            try {
                //无论执行是否成功，方法之后完成之后都必须删除这个key
                redisDao.delete(lockKey);
            } catch (Exception e) {
                log.warn("删除 Redis 锁失败，键：[{}]，错误信息：{}", lockKey, e.getMessage());
            }
        }
        return proceed;
    }

    /**
     * 获取sqpl表达式内容
     * @param objects 参数信息
     * @param lockKey 锁定key
     * @return
     */
    private String getLockKeyContent(Object[] objects,String lockKey){
        try {
            if (lockKey.contains("#l") && objects != null && objects.length > 0){
                SpelParse spelParse = SpelParse.create();
                for (int i = 0; i < objects.length; i ++) {
                    spelParse.setVariable("l" + i,objects[i]);
                }
                return spelParse.parseExpression(lockKey);
            }
            return lockKey;
        }catch (Exception ex){
            log.error("解析表达式错误"+ex.getMessage(),ex);
            return lockKey;
        }
    }
}
