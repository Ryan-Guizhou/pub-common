package com.peach.common.log;

import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@EnableScheduling
public class UserOperLogTask {

    @Resource
    private AbstractLogService logService;

    /**
     * 单例模式获取队列信息
     */
    private final LogQueue logQueue = LogQueue.getInstance();;


    /**
     * 执行定时任务，处理登录日志、操作日志
     */
    @Scheduled(fixedRate = 10_000)
    public void execute() {
        handleUserOperLog();
        handlerLoginLog();
    }

    /**
     * 处理操作日志
     */
    public void handleUserOperLog() {
        List<Map<String, Object>> allUserOperLog = logQueue.getAllUserOperLog();
        if (PeachCollectionUtil.isEmpty(allUserOperLog)) {
            log.warn("本次定时任务执行需要插入的数据为空");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("UserOperLogTask->handleUserOperLog has been executed");
        logService.batchInsertOperLog(allUserOperLog);
        stopWatch.stop();
        log.info("UserOperLogTask has been executed in {}", stopWatch.getTotalTimeMillis());
        log.error(stopWatch.prettyPrint());
    }

    /**
     * 处理登录日志
     */
    public void handlerLoginLog() {
        List<Map<String, Object>> allLoginLog = logQueue.getAllLoginLog();
        if (PeachCollectionUtil.isEmpty(allLoginLog)) {
            log.warn("本次定时任务执行需要插入的数据为空");
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
