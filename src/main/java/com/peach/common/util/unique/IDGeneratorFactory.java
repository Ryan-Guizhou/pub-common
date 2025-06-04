package com.peach.common.util.unique;

import com.peach.common.enums.IDStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 14:13
 */
@Slf4j
public class IDGeneratorFactory {

    private static final Map<IDStrategy, Supplier<AbstractIDGenerator>> REGISTRY = new ConcurrentHashMap<>();

    static {
        REGISTRY.put(IDStrategy.UUID, UUIDGenerator::new);
        REGISTRY.put(IDStrategy.ULID, ULIDGenerator::new);
        REGISTRY.put(IDStrategy.NANOID, NanoIDGenerator::new);
        REGISTRY.put(IDStrategy.SNOWFLAKE, SnowflakeIDGenerator::new);
    }

    private IDGeneratorFactory(){

    }

    public static AbstractIDGenerator createInstance(final IDStrategy IDStrategy) {
        Supplier<AbstractIDGenerator> supplier = REGISTRY.get(IDStrategy);
        Optional.ofNullable(supplier).orElseThrow(() -> new IllegalArgumentException("Unsupported strategy: " + IDStrategy));
        return supplier.get();
    }


}
