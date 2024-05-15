package com.shj.springchatting.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        String authName = "JWT";  // 기능 타이틀명
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authName);

        Components components = new Components()
                .addSecuritySchemes(
                        authName,
                        new SecurityScheme()
                                .name(authName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                                .description("'Bearer '을 제외한 Access Token 입력하세요.")
                );

        Server localServer = new Server();
        localServer.description("Http local server")
                .url("http://localhost:8080");
        Server prodServer = new Server();
        prodServer.description("Https prod server")
                .url("https://www.dev-race.site");

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(apiInfo())
                .servers(Arrays.asList(prodServer, localServer));
    }

    private Info apiInfo() {
        return new Info()
                .title("프로젝트 API")
                .description("Rest API 명세서 / <a href=\"https://www.google.com/\" target=\"blank\">WebSocket API 명세서</a>")
                .version("1.0.0");
    }
}