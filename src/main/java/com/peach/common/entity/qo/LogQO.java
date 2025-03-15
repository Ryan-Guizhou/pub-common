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

    @ApiModelProperty(name = "操作等级")
    private String optLevel;

    @ApiModelProperty(name = "操作类型编码")
    private String optTypeCode;

    @ApiModelProperty(name = "模块编码")
    private String moduleCode;

    @ApiModelProperty(name = "创建人名称")
    private String creatorName;

    @ApiModelProperty(name = "开始时间")
    private String startTime;

    @ApiModelProperty(name = "结束时间")
    private String endTime;

    @ApiModelProperty(name = "请求是否成功")
    private String isSuccess;
}
