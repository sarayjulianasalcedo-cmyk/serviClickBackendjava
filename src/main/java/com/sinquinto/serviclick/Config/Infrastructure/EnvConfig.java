package com.sinquinto.serviclick.Config.Infrastructure;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void init() {

        Dotenv dotenv = Dotenv.load();

        System.setProperty("BD_URL", dotenv.get("BD_URL"));
        System.setProperty("USER_NAME", dotenv.get("USER_NAME"));
        System.setProperty("PASSWORD_BD", dotenv.get("PASSWORD_BD"));
        System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
    }
}