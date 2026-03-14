package com.mixfiscal.produtos_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("Mix Fiscal - Produtos API Rest")
                        .description("API RESTful para gerenciamento de produtos com suporte a integração tributária via " +
                                "código NCM. Permite cadastro, consulta, atualização e remoção de produtos.")
                        .version("v1.0.0")
                        .contact( new Contact()
                                .name("Ricardo Silva")
                                .email("pricardo.sribeiro@outlook.com")
                                .url("https://www.linkedin.com/in/pricardosribeiro/")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente local"),
                        new Server()
                                .url("http://api.mixfiscal.com.br")
                                .description("Ambiente Produção")
                ));
    }
}
