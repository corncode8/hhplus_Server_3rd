package hhplus.serverjava.api.util.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "콘서트 예약 서비스 API 명세서",
                description = "hhplus 백엔드 서버구축 API 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig  {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("콘서트 예약 서비스 API v1")
                .pathsToMatch(paths)
                .build();
    }


    // TODO: 토큰 인증 방식 APIKEY 지정
}
