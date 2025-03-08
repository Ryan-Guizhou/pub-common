package com.peach.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
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

@Configuration
public class Knife4jConfiguration {

    /**
     * @description 注册bean
     * @author pandasF
     * @date 2021/8/24 11:30
     * @param:
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    @Bean("defaultApi2")
    public Docket defaultApi2() {
        Contact contact = new Contact("PEACH","https://www.baidu.com/","huanhuanshu48@gmail.com");
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("PEACH-API文档")
                        .description("PEACH-API文档")
                        .termsOfServiceUrl("http://localhost:8877/")
                        .contact(contact)
                        .version("PEACH-1.0.0")
                        .build())
                //分组名称
                .groupName("PEACH-1.0.0")
                .select()
                //这里指定Controller扫描包路径
                .paths(PathSelectors.any())
                .build();
        return docket;
    }


}
