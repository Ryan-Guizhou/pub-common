package com.peach.common.util.encrypt.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.peach.common.constant.EncryptConstant;
import com.peach.common.util.InstanceLazyLoader;
import com.peach.common.util.encrypt.EncryptAbstract;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description Rsa 加解密算法
 * @CreateTime 2025/3/13 12:42
 */
@Slf4j
public class RsaEncrypt extends EncryptAbstract {

    /**
     * 存储RSA密钥的缓存key
     */
    private static final String RSA_KEYS = "GENERATOR:RSA:KEYS";

    /**
     * 初始化rsa锁
     */
    private static final String RSA_LOCK = "GENERATOR:RSA:LOCK";

    /**
     * 私钥的缓存key
     */
    private static final String PRIVATE_KEY = "privateKey";


    /**
     * 密钥的缓存key
     */
    private static final String PUBLIC_KEY = "publicKey";


    /**
     * 缓存RSA密钥的缓存
     */
    private static final Cache<String, Map<String, String>> RSA_PASS_WORD_INFO = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.HOURS).build();

    /**
     * redissonClient
     */
    private static final RedissonClient redissonClient = InstanceLazyLoader.getInstance(RedissonClient.class);


    /**
     * 初始化rsa 密钥
     */
    static {
        initKey();
    }

    @Override
    public String getAlgorithm() {
        return EncryptConstant.RSA;
    }

    @Override
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = initCipher(1, getPublicKey());
        byte[] bytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return byteToHex(bytes);
    }

    @Override
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = initCipher(2, getPrivateKey());
        byte[] bytes = hexToByte(cipherText);
        return new String(cipher.doFinal(bytes));
    }

    /**
     * 本地缓存获取rsa 公私钥
     * @return
     */
    public static Map<String, String> getRsaInfo() {
        Map<String, String> rsaInfo = null;
        try {
            rsaInfo = RSA_PASS_WORD_INFO.get(RSA_KEYS, () -> redissonClient.getMapCache(RSA_KEYS));
            if (MapUtil.isEmpty(rsaInfo) || rsaInfo.get(PRIVATE_KEY) == null || rsaInfo.get(PUBLIC_KEY) == null) {
                initKey();
                rsaInfo = RSA_PASS_WORD_INFO.get(RSA_KEYS, () -> redissonClient.getMapCache(RSA_KEYS));
            }
        } catch (ExecutionException e) {
            log.error("Get RSA error:", e);
        }
        return rsaInfo;
    }

    /**
     * 初始化rsa 密钥
     */
    private static void initKey() {
        RMapCache<String, String> rsaInfo = redissonClient.getMapCache(RSA_KEYS);
        if (MapUtil.isNotEmpty(rsaInfo) && ObjectUtil.isNotNull(rsaInfo.get(PRIVATE_KEY)) && ObjectUtil.isNotNull(rsaInfo.get(PUBLIC_KEY))) {
            RSA_PASS_WORD_INFO.put(RSA_KEYS, rsaInfo);
        }

        RLock lock = redissonClient.getLock(RSA_LOCK);
        try {
            if (!lock.tryLock(10, TimeUnit.SECONDS)) {
                log.info("Initialization of system login rsa failed, please check the redis connection");
                return;
            }
            rsaInfo = redissonClient.getMapCache(RSA_KEYS);
            if (MapUtil.isEmpty(rsaInfo) || rsaInfo.get(PRIVATE_KEY) == null || rsaInfo.get(PUBLIC_KEY) == null) {
                //生成加密密钥
                RSA rsa = new RSA();
                //私钥加密
                String privateKey = rsa.getPrivateKeyBase64();
                String publicKey = rsa.getPublicKeyBase64();
                rsaInfo.put(PRIVATE_KEY, privateKey);
                rsaInfo.put(PUBLIC_KEY, publicKey);
                log.info("Initialization of system login rsa successful");
            }
        } catch (Exception e) {
            log.error("Initialization of system login rsa error:", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlockAsync();
            }
        }
    }

    /**
     * 初始化Cipher
     * @param mode
     * @return
     * @throws Exception
     */
    private Cipher initCipher(int mode,Key key) throws Exception{
        Cipher cipher = Cipher.getInstance(getAlgorithm());
        cipher.init(mode, key);
        return cipher;
    }

    /**
     * 构建私钥Key
     *
     * @return
     */
    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        // 构建密钥规范 进行 Base64 解码
        byte[] decode = Base64.getDecoder().decode(getRsaInfo().get(PRIVATE_KEY));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode);
        // 生成私钥
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 构建公钥Key
     *
     * @return
     */
    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        // 构建密钥规范 进行 Base64 解码
        byte[] decode = Base64.getDecoder().decode(getRsaInfo().get(PUBLIC_KEY));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decode);
        // 生成私钥
        return keyFactory.generatePublic(spec);
    }

}
