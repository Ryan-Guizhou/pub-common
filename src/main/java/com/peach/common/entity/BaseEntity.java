package com.peach.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/7 14:53
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1080653764114388520L;

    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "分页页码")
    private Integer pageNum;

    @ApiModelProperty(value = "排序类型")
    private Integer sortType;

    @ApiModelProperty(value = "排序规则 desc、asc")
    private String orderType;

    public int getPageNum() {
        if (pageNum == null || 0 > pageNum) {
            pageNum = 1;
        }
        return pageNum;
    }

    public int getPageSize() {
        if (pageSize == null || 0 > pageSize) {
            pageSize = 20;
        }
        return pageSize;
    }


}
