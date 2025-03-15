package com.peach.common.auto;

import com.peach.common.anno.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${knife4j.host:http://localhost:8888}")
    private String host;

    /**
     * @description 注册bean
     * @author pandasF
     * @date 2021/8/24 11:30
     * @param:
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    @Bean
    public Docket commonApi() {
        Contact contact = new Contact("PEACH","https://github.com/Ryan-Guizhou","huanhuanshu48@gmail.com");
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("PEACH-API文档")
                        .description("PEACH-API文档")
                        .termsOfServiceUrl(host)
                        .contact(contact)
                        .version("PEACH-1.0.0")
                        .build())
                //分组名称
                .groupName("通用模块API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.peach.common"))
                .build();
        log.error("knife4j common has been configured");
        return docket;
    }

}
