package com.peach.common.constant;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/5/31 19:14
 */
public interface RedisConstant {

    /**
     * 滑块验证码相关
     */
    String KEY_VALIDATE_IMG = "AUTH:CAPTCHA:KEY_VALIDATE_IMG";

    String KEY_VALIDATE_TPL = "AUTH:CAPTCHA:KEY_VALIDATE_TPL";

    String KEY_VALIDATE_TOKEN = "AUTH:CAPTCHA:KEY_VALIDATE_TOKEN";

    Long EXPIRE_VALIDATE_IMG = 6L;

    Long EXPIRE_VALIDATE_TPL = 6L;

    Long EXPIRE_VALIDATE_TOKEN = 300L;

    /**
     * 缓存过期时间 1个小时
     */
    Long EXPIRE_VALIDATE_HOUR = 3600L;

    /**
     * 缓存过期时间 1天
     */
    Long EXPIRE_VALIDATE_DAY = 84600L;

    /**
     * 预热相关key
     */
    String PRE_HEAT_REDIS_KEY = "PRE_HEAT:WITHE_LIST";

    /**
     * 预热敏感词相关key
     */
    String PRE_SENSITIVE_REDIS_KEY = "PRE_HEAT:SENSITIVE_WORD";



}
