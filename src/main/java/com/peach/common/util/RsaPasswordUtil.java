package com.peach.common.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.encrypt.EncryptAbstract;
import com.peach.common.util.encrypt.EncryptFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 16:00
 */
@Slf4j
public class RsaPasswordUtil {

    /**
     * 将密码通过RSA算法解密 然后加密成md5
     * @param password
     * @return
     */
    public static String realPassword(String password) {
        if (StringUtil.isBlank(password)){
            log.error("The password that needs to be decrypted is empty");
            return StringUtil.EMPTY;
        }
        EncryptAbstract instance = EncryptFactory.getInstance(EncryptConstant.RSA);
        String decryptPassword;
        try {
            decryptPassword = instance.decrypt(password);
        } catch (Exception e) {
            log.error("");
            throw new RuntimeException(e);
        }
        return decryptPassword;
    }

    /**
     * 获取解密之后的md5Hex密码
     * @param password
     * @return
     */
    public static String md5HexPasswd(String password){
        String realPassword = realPassword(password);
        return DigestUtil.md5Hex(realPassword);
    }

}
