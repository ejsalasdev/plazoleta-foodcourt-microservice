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
                        **Microservicio de gestión de restaurantes, menús y pedidos para la plataforma Plazoleta**
                        
                        Este microservicio administra restaurantes, platos y el ciclo de vida de los pedidos.
                        Implementa arquitectura hexagonal y DDD.
                        
                        ## Características principales:
                        - ✅ Gestión de restaurantes y menús
                        - ✅ Validaciones de negocio robustas
                        - ✅ Arquitectura hexagonal + DDD
                        - ✅ Integración con microservicios de usuarios y trazabilidad
                        
                        """,
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Desarrollo Local",
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
