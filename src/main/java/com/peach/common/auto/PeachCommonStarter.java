package com.peach.common.auto;

import com.peach.common.anno.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 17:03
 */
@Slf4j
@Indexed
@Configuration
@MapperScan(lazyInitialization = "true", basePackages = "com.peach.common.dao",
        annotationClass = MyBatisDao.class,sqlSessionFactoryRef = "mybatis-session")
@ComponentScan("com.peach.common")
public class PeachCommonStarter {

}
