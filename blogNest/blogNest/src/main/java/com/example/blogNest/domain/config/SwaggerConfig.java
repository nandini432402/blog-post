package com.example.blogNest.domain.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI Configuration for BlogNest API Documentation
 *
 * Provides comprehensive API documentation with:
 * - JWT authentication setup
 * - API endpoints documentation
 * - Request/response models
 * - Interactive testing interface
 *
 * Access documentation at: /swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI blogNestOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Development Server"),
                        new Server().url("https://api.blognest.com").description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title("BlogNest API")
                .description("Comprehensive REST API for BlogNest - A Social Media Blogging Platform\n\n" +
                        "## Features\n" +
                        "- **User Management**: Registration, authentication, profiles\n" +
                        "- **Blog Management**: Create, edit, delete, publish blog posts\n" +
                        "- **Social Features**: Like, comment, follow users\n" +
                        "- **Content Organization**: Categories, tags, search\n" +
                        "- **Media Upload**: Image and file upload capabilities\n" +
                        "- **Admin Panel**: User and content moderation\n\n" +
                        "## Authentication\n" +
                        "This API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:\n" +
                        "`Authorization: Bearer <your-jwt-token>`\n\n" +
                        "## Getting Started\n" +
                        "1. Register a new user account via `/api/auth/register`\n" +
                        "2. Login to get JWT token via `/api/auth/login`\n" +
                        "3. Use the token to access protected endpoints\n\n" +
                        "## Rate Limiting\n" +
                        "API calls are rate limited to prevent abuse. Current limits:\n" +
                        "- 100 requests per minute for authenticated users\n" +
                        "- 20 requests per minute for unauthenticated users")
                .version(appVersion)
                .contact(new Contact()
                        .name("BlogNest Development Team")
                        .email("api@blognest.com")
                        .url("https://blognest.com/contact"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("Enter JWT token obtained from login endpoint. Example: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'");
    }
}