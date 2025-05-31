package com.peach.common.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Consumer;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description Mybatis批处理工具类
 * @CreateTime 2024/10/14 15:51
 */
public class MyBatisBatchUtil {

    /**
     * 通用 MyBatis 批处理执行器（支持多个 Mapper，函数式调用）
     *
     * @param sqlSessionFactory SqlSessionFactory
     * @param consumer          使用 SqlSession 执行逻辑（可获取多个 Mapper）
     */
    public static void executeBatch(SqlSessionFactory sqlSessionFactory, Consumer<SqlSession> consumer) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            try {
                consumer.accept(sqlSession);     // 执行业务逻辑
                sqlSession.commit();             // 提交事务
            } catch (Exception e) {
                sqlSession.rollback();           // 回滚事务
                throw new RuntimeException("批处理执行失败", e);
            } finally {
                sqlSession.clearCache();         // 清理缓存
            }
        }
    }
}
