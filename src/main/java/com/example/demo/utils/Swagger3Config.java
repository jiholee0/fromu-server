package com.example.demo.utils;

//import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Configuration
public class Swagger3Config {

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.OAS_30)
                    .useDefaultResponseMessages(false)  // Response 응답 메시지 디폴트값 적용 X
                    .securityContexts(Arrays.asList(securityContext()))
                    .securitySchemes(Arrays.asList(apiKey()))
                    .select()
                    .apis(RequestHandlerSelectors.any()) // 모든 RequestMapping URI 추출
                    .paths(PathSelectors.ant("/app/**")) // 경로 패턴 URI만 추출
                    //.apis(RequestHandlerSelectors.basePackage("com.example.demo.src.controller")) // 패키지 기준 추출
                    //.paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo())
                    .tags(new Tag("TEST","테스트 API", 0),
                            new Tag("VIEW", "뷰 API", 1),
                            new Tag("USER","유저 등록/조회/수정/삭제 API", 2),
                            new Tag("COUPLE","커플 등록/조회/수정/삭제 API", 3),
                            new Tag("DIARYBOOK","일기장 등록/조회/수정/삭제 API", 4),
                            new Tag("DIARY","일기 등록/조회/수정/삭제 API", 5),
                            new Tag("PUSH", "푸시 알람 API",10));
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

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("X-ACCESS-TOKEN", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("X-ACCESS-TOKEN", "Authorization", "header");
    }
    }