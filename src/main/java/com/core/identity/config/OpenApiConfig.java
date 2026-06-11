package com.core.identity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Advanced Authentication & Security Identity API")
                        .version("1.0.0")
                        .description("Bu layihə, asinxron OTP doğrulama, anti-bruteforce sorğu limiti (Rate Limiting) " +
                                "və çoxlu cihaz seanslarının təhlükəsizliyi (Session Hijacking Protection) " +
                                "ilə təchiz olunmuş qabaqcıl autentifikasiya sistemidir.")
                );
    }
}