package com.peach.common.util.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description // 加解密算法抽象类
 * @CreateTime 2025/3/13 12:23
 */
@Slf4j
public abstract class EncryptAbstract {


    public abstract String getAlgorithm();

    /**
     * AES 加密
     * @param plainText 明文
     * @return 加密后的 Base64 字符串
     */
    public abstract String encrypt(String plainText) throws Exception;


    /**
     * AES 解密
     * @param cipherText Base64 加密内容
     * @return 解密后的字符串
     */
    public abstract String decrypt(String cipherText) throws Exception;

    public abstract Map<String, String> getRsaInfo() throws Exception;


    /**
     * 16进制转byte
     * @param ciphertext
     * @return
     */
    protected byte[] hexToByte(String ciphertext) {
        byte[] cipherBytes = ciphertext.getBytes();
        if ((cipherBytes.length % 2) != 0) {
            log.error("The content:[{}] length is not even",ciphertext);
            throw new IllegalArgumentException(String.format("The content:[%s] length is not even",ciphertext));
        }
        byte[] result = new byte[cipherBytes.length / 2];
        for (int i = 0; i < cipherBytes.length; i += 2) {
            String item = new String(cipherBytes, i, 2);
            result[i / 2] = (byte) Integer.parseInt(item, 16);
        }
        return result;

    }

    /**
     * byte 转16进制
     * @param bytes
     * @return
     */
    protected String byteToHex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bytes.length == 0){
            return new String(stringBuffer);
        }
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xFF);
            if (1 == s.length()) {
                stringBuffer.append("0");
            }
            stringBuffer = stringBuffer.append(s);
        }
        return new String(stringBuffer);

    }
}
