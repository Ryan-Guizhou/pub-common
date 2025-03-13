package com.peach.common.mongo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Indexed;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/13 16:45
 */
@Slf4j
@Indexed
public abstract class AbstractMongoService {

    @Resource
    MongoTemplate mongoTemplate;

    // **使用 Caffeine 缓存 collection**
    protected static final Cache<String, MongoCollection<Document>> COLLECTION_CACHE = Caffeine.newBuilder()
            .maximumSize(100) // 最多缓存 100 个集合
            .expireAfterAccess(10, TimeUnit.MINUTES) // 10 分钟无访问自动清理
            .build();



}
