package com.peach.common.entity;

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

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 分页页码
     */
    private Integer pageNum;

    /**
     * 排序类型(看具体功能传参)
     */
    private Integer sortType;
    /**
     * 排序规则 desc、asc(看具体功能传参)
     */
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
