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


}
