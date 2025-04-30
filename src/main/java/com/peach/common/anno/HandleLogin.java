package com.peach.common.anno;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 用于标识登录接口
 * @CreateTime 2025/4/30 19:54
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleLogin {

}
