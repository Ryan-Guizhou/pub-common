package com.peach.common.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.peach.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/13 16:54
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "mongo", name = "enabled", havingValue = "true")
public class MongoConfig {

    @Value("${mongo.uri:mongodb://root:1231231456@47.121.112.66:27017/peach}")
    private String mongoUri;

    @Lazy
    @Bean
    @Primary
    @ConditionalOnMissingBean(MongoTemplate.class)
    public MongoTemplate mongoTemplate() {
        log.info("mongoTemplate has been initialized");
        return new MongoTemplate(mongoDatabaseFactory());
    }

    @Lazy
    @Bean
    @Primary
    public MongoDatabaseFactory mongoDatabaseFactory() {
        MongoClient mongoClient = MongoClients.create(mongoUri);
        return new SimpleMongoClientDatabaseFactory(mongoClient, getDatabaseNameFromUri(mongoUri));
    }

    /**
     * 解析数据库名，例如 "mongodb://root:123@host:port/dbname" 提取 "dbname"
     * @param uri
     * @return
     */
    private String getDatabaseNameFromUri(String uri) {
        if (StringUtil.isBlank(uri)){
            log.error("mongo uri is null");
            return null;
        }
        return uri.substring(uri.lastIndexOf("/") + 1).split("\\?")[0].replace("}", "");
    }


}
