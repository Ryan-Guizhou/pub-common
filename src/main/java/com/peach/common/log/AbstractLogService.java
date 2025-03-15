package com.peach.common.log;

import com.github.pagehelper.PageInfo;
import com.peach.common.entity.qo.LogQO;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:16
 */
public abstract class AbstractLogService {


    /**
     * 批量插入操作日志
     * @param userOperLogList
     */
    protected abstract void batchInsertOperLog(List<Map<String,Object>> userOperLogList);

    /**
     * 批量插入登录日志
     * @param LoginLogList
     */
    protected abstract void batchInsertLoginLog(List<Map<String,Object>> LoginLogList);

    /**
     * 分页查询操作日志
     * @param logQO
     * @return PageResult
     */
    public abstract PageInfo selectOperLog(LogQO logQO);

    /**
     * 分页查询登录日志
     * @param logQO
     * @return PageResult
     */
    public abstract PageInfo selectLoginLog(LogQO logQO);

}
