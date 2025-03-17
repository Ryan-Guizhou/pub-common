package com.peach.common.log;

import com.google.common.collect.Lists;
import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:27
 */
@Slf4j
public class LogQueue {

    private static final LogQueue INSTANCE = new LogQueue();

    private final BlockingQueue<Map<String,Object>> userOperLogQueue = new LinkedBlockingQueue<>();

    private final BlockingQueue<Map<String,Object>> loginLogQueue = new LinkedBlockingQueue<>();

    private LogQueue() {} // 私有构造函数

    public static LogQueue getInstance() {
        return INSTANCE;
    }


    /**
     * 添加数据到队列中
     * @param userOperLog
     */
    public void addOperLogQueue(Map<String,Object> userOperLog) {
        userOperLogQueue.add(userOperLog);
    }

    /**
     * 一次性弹出所有数据
     * @return
     */
    public List<Map<String,Object>> getAllUserOperLog() {
        List<Map<String,Object>> resultList = Lists.newArrayList();
        if (PeachCollectionUtil.isEmpty(userOperLogQueue)){
            return resultList;
        }
        userOperLogQueue.drainTo(resultList);
        return resultList;
    }

    /**
     * 添加数据到队列中
     * @param loginLog
     */
    public void addLoginLogQueue(Map<String,Object> loginLog) {
        loginLogQueue.add(loginLog);
    }

    /**
     * 一次性弹出所有数据
     * @return
     */
    public List<Map<String,Object>> getAllLoginLog() {
        List<Map<String,Object> > resultList = Lists.newArrayList();
        if (PeachCollectionUtil.isEmpty(loginLogQueue)){
            return Lists.newArrayList();
        }
        loginLogQueue.drainTo(resultList);
        return resultList;
    }

}
