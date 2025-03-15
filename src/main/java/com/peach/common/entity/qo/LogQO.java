package com.peach.common.entity.qo;

import com.peach.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 13:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogQO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作等级
     */
    private String optLevel;

    /**
     * 操作类型编码
     */
    private String optTypeCode;

    /**
     * 模块编码
     */
    private String moduleCode;


    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 请求是否成功
     */
    private String isSuccess;
}
