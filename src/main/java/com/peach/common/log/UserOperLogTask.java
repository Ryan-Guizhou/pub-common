package com.peach.common.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    public UserOperLogTask() {
        this.logQueue = LogQueue.getInstance();
    }

    @Scheduled(fixedRate = 10000)
    public void handleUserOperLog() {
        List<Map<String, Object>> allUserOperLog = logQueue.getAllUserOperLog();
        if (allUserOperLog == null) {
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

    @Scheduled(fixedRate = 10000)
    public void handlerLoginLog() {
        List<Map<String, Object>> allLoginLog = logQueue.getAllLoginLog();
        if (allLoginLog == null) {
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
}
