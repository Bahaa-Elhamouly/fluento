package com.fluento.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI fluentoOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Fluento API")
                                .description(
                                        "Backend API for the Fluento learning platform"
                                )
                                .version("v1")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME)
                                                .type(
                                                        SecurityScheme.Type.HTTP
                                                )
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}