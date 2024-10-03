package com.nexos.inventario.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Prueba nexos - Sistema de inventario",
                version = "1.0.0",
                description = "Sistema de inventario"
        )
)
public class OpenApiConfig {
}
