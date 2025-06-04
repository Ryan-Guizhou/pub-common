package com.peach.common.envent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/4 22:33
 */
@Slf4j
public abstract class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.onApplicationReady(event);
    }

    /**
     * 子类实现
     * @param event
     */
    protected abstract void onApplicationReady(ApplicationReadyEvent event);

}
