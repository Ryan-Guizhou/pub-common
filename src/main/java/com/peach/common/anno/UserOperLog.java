package com.peach.common.anno;

import com.peach.common.constant.LogLevel;
import com.peach.common.enums.ModuleEnum;
import com.peach.common.enums.OptTypeEnum;
import com.peach.common.util.StringUtil;

import java.lang.annotation.*;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description // 用户操作记录日志
 * @CreateTime 2024/10/14 15:51
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserOperLog {


    /**
     * 模块编码
     * @return
     */
    ModuleEnum moduleCode() default ModuleEnum.COMMON;

    /**
     * 操作类型
     * @return
     */
    OptTypeEnum optType() default OptTypeEnum.DELETE;

    /**
     * 用户操作了什么内容
     * @return
     */
    String optContent() default StringUtil.EMPTY;

    /**
     * 操作级别 INSERT:INFO UPDATE:DEBUG DELETE:WARN
     * @return
     */
    String optLevel() default LogLevel.INFO;
}
