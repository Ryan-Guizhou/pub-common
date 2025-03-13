package com.peach.common.util.encrypt;

import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.encrypt.service.AesEncrypt;
import com.peach.common.util.encrypt.service.DesEncrypt;
import com.peach.common.util.encrypt.service.RsaEncrypt;


/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/13 12:22
 */
public class EncryptFactory {

    public static EncryptAbstract getInstance(String encryptType) {
        switch (encryptType) {
            case EncryptConstant.DES:
                return new DesEncrypt();
            case EncryptConstant.AES:
                return new AesEncrypt();
            case EncryptConstant.RSA:
                return new RsaEncrypt();
            default:
                return null;
        }

    }
}
