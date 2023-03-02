package com.example.demo.utils;

//import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Comparator;

@Configuration
@Slf4j
public class Swagger3Config {

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.OAS_30)
                    .useDefaultResponseMessages(false)  // Response 응답 메시지 디폴트값 적용 X
                    .select()
                    .apis(RequestHandlerSelectors.any()) // 모든 RequestMapping URI 추출
                    .paths(PathSelectors.ant("/app/**")) // 경로 패턴 URI만 추출
                    //.apis(RequestHandlerSelectors.basePackage("com.example.demo.src.controller")) // 패키지 기준 추출
                    //.paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo())
                    .tags(new Tag("UserController","User-Controller", 1),
                            new Tag("CoupleController","Couple-Controller", 2));
//                    .operationOrdering(new Comparator<Operation>() {
//                        @Override
//                        public int compare(Operation o1, Operation o2) {
//                            log.info("o1>> {}", o1.getUniqueId());
//                            log.info("o2>> {}", o2.getUniqueId());
//                            return o1.getUniqueId().compareTo(o2.getUniqueId());
//                        }
//                    });


        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("FromU API")
                    .description("FromU api 명세서")
                    .version("v0.0.1")
                    //.termsOfServiceUrl("서비스 약관 URL")
                    //.contact("dlwlgh1254@gmail.com")
                    //.license("License")
                    //.licenseUrl("localhost:8080")
                    //.licenseUrl("3.39.249.248:8080")
                    .build();
        }
    }