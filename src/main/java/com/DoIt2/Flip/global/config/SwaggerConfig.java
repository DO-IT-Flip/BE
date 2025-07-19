package com.DoIt2.Flip.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Flip API 명세서",
                version = "v1.0.0",
                description = "일정 서비스 Flip의 API 문서입니다."
        )
)
@Configuration
public class SwaggerConfig {
}
