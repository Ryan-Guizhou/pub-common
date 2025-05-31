package com.peach.common.util.encrypt;

import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.encrypt.service.AesEncrypt;
import com.peach.common.util.encrypt.service.DesEncrypt;
import com.peach.common.util.encrypt.service.RsaEncrypt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/13 12:22
 */
public class EncryptFactory {

    /**
     * 缓存实例，使用单例模式
     */
    private static final Map<String,EncryptAbstract> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 根据加密类型获取加密实例
     * @param encryptType
     * @return
     */
    public static EncryptAbstract getInstance(String encryptType) {
        return INSTANCE_MAP.computeIfAbsent(encryptType, type -> {
            switch (type) {
                case EncryptConstant.DES:
                    return new DesEncrypt();
                case EncryptConstant.AES:
                    return new AesEncrypt();
                case EncryptConstant.RSA:
                    return new RsaEncrypt();
                default:
                    throw new IllegalArgumentException("Unsupported encrypt type: " + type);
            }
        });
    }
}
