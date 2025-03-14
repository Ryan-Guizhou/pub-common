package com.peach.common.log;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:27
 */
public class LogQueue {

    private static final LogQueue INSTANCE = new LogQueue();

    private final Queue<Map<String,Object>> userOperLogQueue = new LinkedBlockingQueue<>();

    private final Queue<Map<String,Object>> loginLogQueue = new LinkedBlockingQueue<>();

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
        List<Map<String, Object>> allLogs = new ArrayList<>();

        // 循环弹出队列中的所有元素
        Map<String, Object> log;
        while ((log = userOperLogQueue.poll()) != null) {
            allLogs.add(log);
        }
        return allLogs;
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
        List<Map<String, Object>> allLogs = new ArrayList<>();

        // 循环弹出队列中的所有元素
        Map<String, Object> log;
        while ((log = loginLogQueue.poll()) != null) {
            allLogs.add(log);
        }
        return allLogs;
    }

}
