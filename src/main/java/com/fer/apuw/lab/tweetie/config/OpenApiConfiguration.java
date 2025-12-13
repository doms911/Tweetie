package com.fer.apuw.lab.tweetie.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tweetie API")
                        .version("1.0.0")
                        .description("REST API documentation for Tweetie")
                        .contact(new Contact().name("Dominik").email("dominik.barukcic@fer.hr")))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub repository")
                        .url("https://github.com/doms911/Tweetie"));
    }
}
