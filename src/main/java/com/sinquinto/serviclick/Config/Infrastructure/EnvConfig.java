package com.sinquinto.serviclick.Config.Infrastructure;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void init() {
        // En Docker las variables de entorno ya están inyectadas por docker-compose
        // No necesitamos cargar el .env manualmente
    }
}