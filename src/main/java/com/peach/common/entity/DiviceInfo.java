package com.peach.common.entity;

import lombok.Data;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:02
 */
@Data
public class DiviceInfo{

    /**
     * 操作系统
     */
    private String os;

    /**
     * 私网IP
     */
    private String privateIp;

    /**
     * 公网IP
     */
    private String publicIp;

    /**
     *
     */
    private String device;

    /**
     * 浏览器信息
     */
    private String browser;

}