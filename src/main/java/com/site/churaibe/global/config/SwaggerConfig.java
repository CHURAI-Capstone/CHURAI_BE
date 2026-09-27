package com.site.churaibe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String JWT_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI openAPI() {
        Info apiInfo = new Info()
                .title("CHURAI-BE API")
                .description("CHURAI 백엔드 API 명세서입니다.")
                .version("v1.0.0");

        // Authorize 버튼에 액세스 토큰만 입력하면 Authorization: Bearer {token} 헤더로 전송됨
        SecurityScheme jwtScheme = new SecurityScheme()
                .name(JWT_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(apiInfo)
                .components(new Components().addSecuritySchemes(JWT_SCHEME_NAME, jwtScheme))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME));
    }
}
