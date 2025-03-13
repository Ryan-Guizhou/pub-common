package com.peach.common.constant;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2024/10/10 16:16
 */
public interface RedisModeConstant {

    /**
     * 单级模式
     */
    String STANDALONE = "standalone";

    /**
     * 哨兵模式
     */
    String SENTINEL = "sentinel";

    /**
     * 集群模式
     */
    String CLUSTER = "cluster";
}
