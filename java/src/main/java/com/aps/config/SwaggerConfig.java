package com.aps.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cálculo de Emergia - APS")
                        .version("1.0")
                        .description("API para gerenciamento de processos, fluxos e cálculo de indicadores de emergia.")
                        .contact(new Contact()
                                .name("Equipe APS Engenharia de Software")));
    }
}
