package com.peach.common.util.unique;

import com.peach.common.enums.IDStrategy;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 14:16
 */
@Slf4j
public class ULIDGenerator extends AbstractIDGenerator implements IDGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int ID_LENGTH = 26;

    @Override
    public String doGenerateId() {
        // 使用当前时间戳+随机数拼接成128bit
        long timestamp = System.currentTimeMillis();
        byte[] randomBytes = new byte[10];
        RANDOM.nextBytes(randomBytes);

        // 将时间戳和随机数组合成一个byte数组（16字节，128位）
        byte[] ulidBytes = new byte[16];
        // 48位时间戳放入前6字节
        for (int i = 5; i >= 0; i--) {
            ulidBytes[i] = (byte)(timestamp & 0xFF);
            timestamp >>= 8;
        }
        // 后10字节放随机数
        System.arraycopy(randomBytes, 0, ulidBytes, 6, 10);

        // 把128位当成一个大整数，用62进制编码，生成26位字符串
        return encodeBase62(ulidBytes);
    }

    @Override
    public IDStrategy strategy() {
        return IDStrategy.ULID;
    }

    private static String encodeBase62(byte[] data) {
        // data是16字节大整数
        // 62进制需要的位数：
        // 62^26 ≈ 2^150，所以128位足够26位编码
        // 直接使用BigInteger处理
        java.math.BigInteger number = new java.math.BigInteger(1, data);
        StringBuilder sb = new StringBuilder();

        while (number.compareTo(java.math.BigInteger.ZERO) > 0) {
            int rem = number.mod(java.math.BigInteger.valueOf(62)).intValue();
            sb.append(ALPHABET.toCharArray()[rem]);
            number = number.divide(java.math.BigInteger.valueOf(62));
        }

        // 不足26位则补0
        while (sb.length() < ID_LENGTH) {
            sb.append('0');
        }

        return sb.reverse().toString();
    }
}
