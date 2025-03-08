package com.peach.common.config;

import com.peach.common.filter.CostTimeFiler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 18:03
 */
@Configuration
public class WebConfig {

    @Bean
    public CostTimeFiler costTimeFiler() {
        return new CostTimeFiler();
    }
}
