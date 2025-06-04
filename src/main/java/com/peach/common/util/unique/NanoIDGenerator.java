package com.peach.common.util.unique;

import com.peach.common.enums.IDStrategy;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 14:14
 */
@Slf4j
public class NanoIDGenerator extends AbstractIDGenerator implements IDGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String doGenerateId() {
        return generateNanoId(ALPHABET,MAX_LENGTH);
    }

    /**
     * 生成NanoID
     * @param alphabet 自定义字符集
     * @param size 长度
     * @return
     */
    public String generateNanoId(String alphabet, int size) {
        StringBuilder sb = new StringBuilder(size);
        int mask = (2 << (int)Math.floor(Math.log(alphabet.length()) / Math.log(2))) - 1;
        while (sb.length() < size) {
            int rnd = RANDOM.nextInt();
            for (int i = 0; i < 4 && sb.length() < size; i++) {
                int index = (rnd >> (i * 8)) & mask;
                if (index < alphabet.length()) {
                    sb.append(alphabet.charAt(index));
                }
            }
        }
        return String.valueOf(sb);
    }

    @Override
    public IDStrategy strategy() {
        return IDStrategy.NANOID;
    }
}
