package com.peach.common.anno;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/5/1 16:28
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {

    String message() default "字段不能为空,或者空字符串";

}
