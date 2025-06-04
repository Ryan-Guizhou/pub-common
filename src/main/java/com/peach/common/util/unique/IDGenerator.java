package com.peach.common.util.unique;

import com.peach.common.enums.IDStrategy;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 14:13
 */
public interface IDGenerator {


    /**
     * 最大长度为32位
     */
    Integer MAX_LENGTH = 32;

    /**
     * 自定义字符集 用于生成NanoId和ULID
     */
    String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    /**
     * 生成32位固定长度的ID字符串
     * @return
     */
    String generateId();

    IDStrategy strategy();
}
