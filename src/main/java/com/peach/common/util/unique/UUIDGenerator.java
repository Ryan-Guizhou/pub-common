package com.peach.common.util.unique;

import com.peach.common.enums.IDStrategy;
import com.peach.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 14:14
 */
@Slf4j
public class UUIDGenerator extends AbstractIDGenerator implements IDGenerator {

    /**
     * 开始的索引值
     */
    public static final Integer START_INDEX = 0;

    /**
     * UUID 分隔符
     */
    public static final String UUID_SEPARATOR = "-";

    /**
     * 默认分隔符
     */
    public static final String DEFAULE_SEPARATOR = StringUtil.EMPTY;


    @Override
    public String doGenerateId() {
        String uuid = UUID.randomUUID().toString();
        String replace = uuid.replace(UUID_SEPARATOR, DEFAULE_SEPARATOR);
        return replace.substring(START_INDEX, MAX_LENGTH);
    }

    @Override
    public IDStrategy strategy() {
        return IDStrategy.UUID;
    }
}
