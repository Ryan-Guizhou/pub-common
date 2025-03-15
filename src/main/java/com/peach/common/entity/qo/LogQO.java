package com.peach.common.entity.qo;

import com.peach.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "日志请求模型")
@EqualsAndHashCode(callSuper = true)
public class LogQO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作等级",required = true)
    private String optLevel;

    @ApiModelProperty(value = "操作类型编码")
    private String optTypeCode;

    @ApiModelProperty(value = "模块编码")
    private String moduleCode;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "请求是否成功")
    private String isSuccess;
}
