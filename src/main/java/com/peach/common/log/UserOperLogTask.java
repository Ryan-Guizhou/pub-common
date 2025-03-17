package com.peach.common.log;

import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;
import org.springframework.util.StopWatch;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:37
 */
@Slf4j
@Indexed
@Component
public class UserOperLogTask {

    @Resource
    private AbstractLogService logService;

    private final LogQueue logQueue;

    private final ScheduledExecutorService executorService;

    public UserOperLogTask() {
        this.logQueue = LogQueue.getInstance();
        this.executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            handleUserOperLog();
            handlerLoginLog();
        }, 0, 1, java.util.concurrent.TimeUnit.MINUTES);
    }


    public void handleUserOperLog() {
        List<Map<String, Object>> allUserOperLog = logQueue.getAllUserOperLog();
        if (PeachCollectionUtil.isEmpty(allUserOperLog)) {
            log.error("本次定时任务执行需要插入的数据为空");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("UserOperLogTask->handleUserOperLog has been executed");
        logService.batchInsertOperLog(allUserOperLog);
        stopWatch.stop();
        log.info("UserOperLogTask has been executed in {}", stopWatch.getTotalTimeMillis());
        log.error(stopWatch.prettyPrint());
    }

    public void handlerLoginLog() {
        List<Map<String, Object>> allLoginLog = logQueue.getAllLoginLog();
        if (PeachCollectionUtil.isEmpty(allLoginLog)) {
            log.error("本次定时任务执行需要插入的数据为空");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("UserOperLogTask->handlerLoginLog has been executed");
        logService.batchInsertLoginLog(allLoginLog);
        stopWatch.stop();
        log.info("UserOperLogTask has been executed in {}", stopWatch.getTotalTimeMillis());
        log.error(stopWatch.prettyPrint());
    }

    /**
     * 释放资源，防止线程池泄漏
     */
    @PreDestroy
    public void shutdownExecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            log.info("正在关闭 UserOperLogTask 线程池...");
            executorService.shutdown();
        }
    }
}
