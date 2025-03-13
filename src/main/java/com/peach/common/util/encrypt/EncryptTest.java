package com.peach.common.util.encrypt;

import com.peach.common.constant.EncryptConstant;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/13 12:30
 */
public class EncryptTest {

    public static void main(String[] args) throws Exception {
        String str = EncryptConstant.RSA;
        EncryptAbstract encrypt = EncryptFactory.getInstance(str);

        String plaintext = "{\"id\":\"1\",\"name\":\"ryan\"}";
        String encrypted = encrypt.encrypt(plaintext);
        System.out.println("🔒 加密后: " + encrypted);
        String decrypted = encrypt.decrypt(encrypted);
        System.out.println("🔓 解密后: " + decrypted);

    }
}
