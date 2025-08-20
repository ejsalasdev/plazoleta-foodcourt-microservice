package com.plazoleta.foodcourtmicroservice.commons.config.openapi;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "🍴 Foodcourt Microservice API",
                version = "v1.0.0",
                description = """
                        **Restaurant, menu and order management microservice for the Plazoleta platform**
                        
                        This microservice manages restaurants, dishes and the order lifecycle.
                        Implements hexagonal architecture and DDD.
                        
                        ## Key Features:
                        - ✅ Restaurant and menu management
                        - ✅ Robust business validations
                        - ✅ Hexagonal architecture + DDD
                        - ✅ Integration with user and traceability microservices
                        
                        """,
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development",
                        url = "http://localhost:8092"
                )
        },
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
