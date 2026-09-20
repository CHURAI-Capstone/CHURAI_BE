package com.site.churaibe.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info apiInfo = new Info()
                .title("CHURAI-BE API")
                .description("CHURAI 백엔드 API 명세서입니다.")
                .version("v1.0.0");

        return new OpenAPI()
                .info(apiInfo);
    }
}