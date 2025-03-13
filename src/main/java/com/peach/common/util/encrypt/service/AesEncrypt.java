package com.peach.common.util.encrypt.service;

import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.encrypt.EncryptAbstract;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description DES 加解密
 * @CreateTime 2025/3/13 10:24
 */
@Slf4j
public class AesEncrypt extends EncryptAbstract {

    /**
     * 私钥
     */
    private final static String PRIVATE_KEY = "PEACH/COMMON/202503/Ryan_Guizhou";


    /**
     * 加密模式
     */
    private static final String CBC_MODEL = "AES/CBC/PKCS5Padding";

    /**
     * 偏移量 必须是8位
     */
    private static final String IV_STRING = "SHA1PRNG20250313";

    @Override
    public String getAlgorithm() {
        return EncryptConstant.AES;
    }

    /**
     * AES 加密
     * @param plainText 明文
     * @return 加密后的 Base64 字符串
     */
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return byteToHex(encryptedBytes);
    }

    /**
     * AES 解密
     * @param cipherText Base64 加密内容
     * @return 解密后的字符串
     */
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
        byte[] decodedBytes = hexToByte(cipherText);
        return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
    }

    /**
     * 初始化 AES Cipher
     * @param mode 加密/解密模式
     * @return Cipher 对象
     */
    private Cipher initCipher(int mode) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8), getAlgorithm());
        IvParameterSpec iv = new IvParameterSpec(IV_STRING.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(CBC_MODEL);
        cipher.init(mode, secretKey, iv);
        return cipher;
    }

}
