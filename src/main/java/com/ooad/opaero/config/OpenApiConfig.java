package com.ooad.opaero.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI opaeroOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Opaero API")
                        .description("Airport and Flight Operations Management APIs")
                        .version("1.0.0"));
    }
}
