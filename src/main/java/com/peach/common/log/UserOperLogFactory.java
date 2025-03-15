package com.peach.common.log;

import cn.hutool.extra.spring.SpringUtil;
import com.peach.common.constant.DbTypeConstant;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:47
 */
public class UserOperLogFactory {


    /**
     * 根据数据库的启用类型获取存储方式
     * @param dbType 数据库类型
     * @return
     */
    public static AbstractLogService getInstance(String dbType) {
        if(DbTypeConstant.MYSQL.equals(dbType)) {
            return SpringUtil.getBean(MysqlLogService.class);
        }else if(DbTypeConstant.MONGODB.equals(dbType)) {
            return SpringUtil.getBean(MongoLogService.class);
        }else {
            return SpringUtil.getBean(MysqlLogService.class);
        }
    }
}
