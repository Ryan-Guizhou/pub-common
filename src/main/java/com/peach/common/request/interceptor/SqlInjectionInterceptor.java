package com.peach.common.request.interceptor;

import cn.hutool.core.util.ArrayUtil;
import com.peach.common.constant.PubCommonConst;
import com.peach.common.exception.IllegalParamExceprion;
import com.peach.common.request.AbstractWrapperInterceptor;
import com.peach.common.request.wrapper.RequestWrapper;
import com.peach.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description sql拦截注入
 * @CreateTime 2025/3/16 18:48
 */
@Slf4j
@Indexed
@Component
public class SqlInjectionInterceptor extends AbstractWrapperInterceptor implements Ordered {

    /**
     * 默认的防止sql注入的表达式
     */
    private static Pattern DEFAULT_SQL_PATTERN = Pattern.compile("(\\\\b(SELECT|UPDATE|DELETE|INSERT|WHERE|DROP|ALTER|TRUNCATE)\\\\b)");

    @Value("peach.common.sqlPattern:")
    private String sqlPattern;

    @PostConstruct
    public void init() {
        DEFAULT_SQL_PATTERN = StringUtil.isBlank(sqlPattern) ? DEFAULT_SQL_PATTERN : Pattern.compile(sqlPattern);
        log.info("SqlPattern: [{}]", DEFAULT_SQL_PATTERN);
    }

    @Override
    protected boolean handleInceptor(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contentType = request.getContentType();
        boolean isPostRequest = StringUtil.isNotBlank(request.getMethod()) && PubCommonConst.REQUEST_POST.equalsIgnoreCase(request.getMethod());
        boolean isRequestBody = StringUtil.isNotBlank(contentType) && contentType.contains(PubCommonConst.CONTENT_TYPE);

        RequestWrapper wrapperRequest = new RequestWrapper(request);
        Optional<String> checkMsg;
        if (isPostRequest && isRequestBody) {
            checkMsg  = checkPost(wrapperRequest);
            if (checkMsg.isPresent()) {
                throw new IllegalParamExceprion(checkMsg.get());
            }
            return PubCommonConst.TRUE;
        }

        boolean isGetRequest = StringUtil.isNotBlank(request.getMethod()) && PubCommonConst.REQUEST_GET.equalsIgnoreCase(request.getMethod());
        if (isGetRequest) {
            checkMsg = checkGet(request);
            if (checkMsg.isPresent()) {
                throw new IllegalParamExceprion(checkMsg.get());
            }
        }
        return PubCommonConst.TRUE;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 校验 POST 请求
     * @param request
     * @return
     */
    private Optional<String> checkPost(RequestWrapper request) {
        try{
            String method = request.getMethod();
            String jsonStr = request.getBody();
            Matcher matcher = DEFAULT_SQL_PATTERN.matcher(jsonStr);
            if (matcher.find()){
                log.error("非法请求，方法: [{}], 请求体: [{}], 错误: [{}]", request.getMethod(), jsonStr, matcher.group());
                return Optional.of(matcher.group());
            }
            return Optional.empty();
        }catch (Exception e) {
            throw new IllegalParamExceprion(e.getMessage());
        }
    }

    /**
     * 校验 GET 请求
     * @param request
     * @return
     */
    private Optional<String> checkGet(HttpServletRequest request) {
        try{
            String method = request.getMethod();
            for(String[] values : request.getParameterMap().values()) {
                if (ArrayUtil.isEmpty(values)) {
                    continue;
                }
                for (String value : values) {
                    if (StringUtil.isBlank(value)) {
                        continue;
                    }
                    Matcher matcher = DEFAULT_SQL_PATTERN.matcher(value);
                    if (matcher.find()) {
                        log.error("非法请求，方法: [{}], 请求参数: [{}], 错误: [{}]", method, Arrays.toString(values), matcher.group());
                        return Optional.of(matcher.group());
                    }
                }
            }
        }catch (Exception e) {
            throw new IllegalParamExceprion(e.getMessage());
        }
        return Optional.empty();
    }
}
