package com.peach.common.util.encrypt.service;

import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.encrypt.EncryptAbstract;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description DES 加解密
 * @CreateTime 2025/3/13 10:24
 */
@Slf4j
public class DesEncrypt extends EncryptAbstract {

    /**
     * 私钥
     */
    private final static String PRIVATE_KEY = "PEACH/COMMON/20250313/Ryan_Guizou";


    /**
     * 加密模式
     */
    private static final String CBC_MODEL = "DES/CBC/PKCS5Padding";

    /**
     * 偏移量 必须是8位
     */
    private static final String IV_STRING = "SHA1PRNG";

    @Override
    public String getAlgorithm() {
        return EncryptConstant.DES;
    }

    /**
     * 加密
     * @param plaintext
     * @return
     */
    public String encrypt(String plaintext) throws Exception{
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = cipher.doFinal(plaintext.getBytes());
        return byteToHex(bytes);


    }

    /**
     * 解密
     * @param plaintext
     * @return
     * @throws Exception
     */
    public String decrypt(String plaintext) throws Exception{
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
        byte[] bytes = hexToByte(plaintext);
        return new String(cipher.doFinal(bytes));

    }

    /**
     * 初始化Cipher
     * @param mode
     * @return
     * @throws Exception
     */
    private Cipher initCipher(int mode) throws Exception{
        SecureRandom secureRandom = new SecureRandom();
        DESKeySpec desKeySpec = new DESKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(getAlgorithm());
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(IV_STRING.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(CBC_MODEL);
        cipher.init(mode, secretKey, iv, secureRandom);
        return cipher;
    }


}
