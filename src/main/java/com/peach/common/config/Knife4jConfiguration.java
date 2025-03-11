package com.peach.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * Swagger2配置信息
 * 这里分了两组显示
 * 第一组是api，当作用户端接口
 * 第二组是admin，当作后台管理接口
 * 也可以根据实际情况来减少或者增加组
 *
 * @author Eric
 * @date 2023-07-30 22:17
 */

@Slf4j
@Configuration
@EnableKnife4j
public class Knife4jConfiguration{

    @Value("knife4j.host:http://localhost:8888")
    private String host;

    /**
     * @description 注册bean
     * @author pandasF
     * @date 2021/8/24 11:30
     * @param:
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    @Bean("defaultApi2")
    public Docket defaultApi2() {
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
                .groupName("PEACH-1.0.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.peach"))
                //这里指定Controller扫描包路径
                .paths(PathSelectors.any())
                .build();
        log.error("knife4j has been configured");
        return docket;
    }


}
