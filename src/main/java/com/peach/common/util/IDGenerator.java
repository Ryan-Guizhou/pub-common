package com.peach.common.util;

import com.peach.common.enums.IDStrategy;
import com.peach.common.util.unique.IDGeneratorFactory;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description // UUID生成器
 * @CreateTime 2024/10/10 15:22
 */
public class IDGenerator {

    public static String UUID() {
        return IDGeneratorFactory.createInstance(IDStrategy.UUID).generateId();
    }

}
