package com.peach.common.auto;

import com.peach.common.anno.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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


    /**
     * @description 注册bean
     * @author pandasF
     * @date 2021/8/24 11:30
     * @param:
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    @Bean
    public Docket commonApi() {
        Contact contact = new Contact("Ryan","https://github.com/Ryan-Guizhou","huanhuanshu48@gmail.com");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("PEACH-API文档")
                        .description("PEACH-API文档")
                        .contact(contact)
                        .version("PEACH-1.0.0")
                        .build())
                //分组名称
                .groupName("COMMON_API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.peach.common"))
                .build();
        log.info("knife4j COMMON_API has been configured");
        return docket;
    }

}
