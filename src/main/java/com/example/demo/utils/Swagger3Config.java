package com.example.demo.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
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
                    .apiInfo(apiInfo());
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