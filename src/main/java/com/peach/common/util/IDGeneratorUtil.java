package com.peach.common.util;

import com.peach.common.enums.IDStrategy;
import com.peach.common.util.unique.IDGeneratorFactory;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 23:16
 */
public class IDGeneratorUtil {

    public static String UUID() {
        return IDGeneratorFactory.createInstance(IDStrategy.UUID).generateId();
    }
}
