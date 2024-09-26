package com.infnet.warehouseservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("PetFriends - API do almoxarifado")
                        .description("Documentação da API para o serviço de almoxarifado.")
                        .version("1.0"));
    }
}
