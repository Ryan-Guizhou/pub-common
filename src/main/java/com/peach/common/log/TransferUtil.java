package com.peach.common.log;

import cn.hutool.json.JSONUtil;
import com.peach.common.anno.UserOperLog;
import com.peach.common.constant.PubCommonConst;
import com.peach.common.entity.DiviceInfo;
import com.peach.common.entity.LoginLogDO;
import com.peach.common.entity.UserOperLogDO;
import com.peach.common.enums.ModuleEnum;
import com.peach.common.enums.OptTypeEnum;
import com.peach.common.log.spel.SpelParse;
import com.peach.common.response.Response;
import com.peach.common.util.DateUtil;
import com.peach.common.util.IDGenerator;
import com.peach.common.util.IpUtil;
import com.peach.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:01
 */
@Slf4j
public class TransferUtil {


    /**
     * 转换成mysql入库实体
     * @param invocation
     * @param operLog
     * @param executionTime
     * @param response
     * @return
     */
    public static Map<String,Object> transferToOperLog(MethodInvocation invocation,  UserOperLog operLog, long executionTime, Response response) {
        HttpServletRequest request = getHttpServletRequest();
        String requestMethod = request.getMethod();
        String optContent = operLog.optContent();
        Object[] arguments = invocation.getArguments();
        optContent = getContent(arguments,optContent);

        ModuleEnum moduleEnum = operLog.moduleCode();
        String moduleCode = moduleEnum == null ? StringUtil.EMPTY : moduleEnum.getModuleCode();

        OptTypeEnum optTypeEnum = operLog.optType();
        String optTypeCode = optTypeEnum == null ? StringUtil.EMPTY : optTypeEnum.getOptTypeCode();

        String optLevel = operLog.optLevel();

        DiviceInfo diviceInfo = getDiviceInfo(request);
        UserOperLogDO userOperLogDO = new UserOperLogDO();
        userOperLogDO.setId(IDGenerator.UUID());
        userOperLogDO.setOptTypeCode(optTypeCode);
        userOperLogDO.setModuleCode(moduleCode);
        //从登录状态中获取
//        userOperLogDO.setCreatorCode();
//        userOperLogDO.setCreatorName();
//        userOperLogDO.setRoleCode();
        userOperLogDO.setOptContent(optContent);
        userOperLogDO.setCreateTime(DateUtil.nowTime());
        userOperLogDO.setOptLevel(optLevel);
        userOperLogDO.setPrivateIp(diviceInfo.getPrivateIp());
        userOperLogDO.setPublicIp(diviceInfo.getPublicIp());
        userOperLogDO.setDevice(diviceInfo.getDevice());
        userOperLogDO.setBrowser(diviceInfo.getBrowser());
        userOperLogDO.setOs(diviceInfo.getOs());
        userOperLogDO.setExecutionTime(executionTime);
        userOperLogDO.setIsSuccess(response.isSuccess() + StringUtil.EMPTY);
        String errorMsg = response.isSuccess() == PubCommonConst.TRUE ? StringUtil.EMPTY : response.getMsg();
        userOperLogDO.setErrorMsg(errorMsg);
        userOperLogDO.setResponseData(JSONUtil.toJsonStr(response.getData()));
        return userOperLogDO.toMap();
    }

    /**
     * 转换成LoginLog
     * @param invocation
     * @param executionTime
     * @param response
     * @return
     */
    public static Map<String,Object> transferToLoginLog(MethodInvocation invocation, long executionTime, Response response) {
        HttpServletRequest request = getHttpServletRequest();
        Object[] arguments = invocation.getArguments();
        DiviceInfo diviceInfo = getDiviceInfo(request);
        LoginLogDO loginLogDO = new LoginLogDO();
        loginLogDO.setId(IDGenerator.UUID());
//        loginLogDO.setCreatorCode();
//        loginLogDO.setCreatorName();
        loginLogDO.setCreateTime(DateUtil.nowTime());
        loginLogDO.setPrivateIp(diviceInfo.getPrivateIp());
        loginLogDO.setPublicIp(diviceInfo.getPublicIp());
        loginLogDO.setDevice(diviceInfo.getDevice());
        loginLogDO.setBrowser(diviceInfo.getBrowser());
        loginLogDO.setOs(diviceInfo.getOs());
        loginLogDO.setExecutionTime(executionTime);
        loginLogDO.setIsSuccess(response.isSuccess() == PubCommonConst.TRUE ? response.getMsg() :StringUtil.EMPTY);
        String errorMsg = response.isSuccess() == PubCommonConst.TRUE ? StringUtil.EMPTY : response.getMsg();
        loginLogDO.setErrorMsg(errorMsg);
        loginLogDO.setResponseData(JSONUtil.toJsonStr(response.getData()));
        return loginLogDO.toMap();
    }


    /**
     * 解析spel
     * @param objects
     * @param content
     * @return
     */
    private static String getContent(Object[] objects,String content){
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
            log.error("spel parse failed"+ex.getMessage(),ex);
            return content;
        }
    }

    /**
     * 获取HttpServletRequest
     * @return
     */
    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attributes).getRequest();
        }
        return null;
    }

    /**
     * 获取Divice信息
     * @param request
     * @return
     */
    private static DiviceInfo getDiviceInfo(HttpServletRequest request) {
        DiviceInfo diviceInfo = new DiviceInfo();
        String userAgent = request.getHeader(PubCommonConst.USER_AGENT);

        // 使用 UserAgentUtils 解析 User-Agent
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        Browser browser = agent.getBrowser();
        OperatingSystem os = agent.getOperatingSystem();

        // 组装设备信息
        diviceInfo.setOs(os.getName());
        diviceInfo.setDevice(agent.getOperatingSystem().getName()); // 这里可以尝试解析设备名称
        diviceInfo.setBrowser(browser.getName());
        diviceInfo.setPrivateIp(IpUtil.getIpAddr(request));
        diviceInfo.setPublicIp(IpUtil.getIpAddr(request));

        return diviceInfo;
    }

}
