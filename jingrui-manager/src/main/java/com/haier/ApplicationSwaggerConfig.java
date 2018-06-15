package com.haier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

//@PropertySource("classpath:properties/config.properties")
//@Profile("!product")
@Configuration
@EnableSwagger2
public class ApplicationSwaggerConfig extends WebMvcConfigurationSupport {

    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。
     * 需要重新指定静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }


    /**
     * 配置servlet处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .tags(new Tag("restservices", "精锐管理平台"), getTags())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.haier"))
                .build()
                .genericModelSubstitutes(Optional.class);
    }

    private Tag[] getTags() {
        Tag[] tags = {
                new Tag("module", "模块管理")
        };

        return tags;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("精锐管理平台接口测试页面")
                .description("开放接口手册 端口：8081")
                .version("1.0.0")
                .termsOfServiceUrl("http://localhost:8081")
                .contact("zs")
//                .license("LICENSE")
//                .licenseUrl("http://localhost:8081  ")
                .build();
    }
}
