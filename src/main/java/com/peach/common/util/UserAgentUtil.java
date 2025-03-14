package com.peach.common.util;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import com.peach.common.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 18:18
 */
@Slf4j
public class UserAgentUtil {


    /**
     * 获取系统信息,如果是模拟请求返回""
     * @param request
     * @return
     */
    public static String getDeviceInfo(HttpServletRequest request){
        ThrowUtil.throwIf(request == null, StatusEnum.PARAM_ERROR, "request can't be null");
        String userAgent = ServletUtil.getHeader(request, "User-Agent", StandardCharsets.UTF_8);
        return getSystemOs(userAgent);
    }

    /**
     * 根据userAgent获取系统信息
     * @param userAgent
     * @return
     */
    private static String getSystemOs(String userAgent){
        if (StringUtils.isBlank(userAgent)){
            return StringUtils.EMPTY;
        }
        try {
            UserAgent userAgentInfo = UserAgentParser.parse(userAgent);
            if (userAgentInfo == null){
                return StringUtils.EMPTY;
            }
            OS os = userAgentInfo.getOs();
            if (os == null){
                return StringUtils.EMPTY;
            }
            String systemOs = os.toString();
            if ("unkonwn".equalsIgnoreCase(systemOs) || "Unknown".equals(systemOs)){
                return StringUtils.EMPTY;
            }
            return systemOs;
        } catch (Exception e) {
            log.error("getSystemOs error"+e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }
}
