package com.sinquinto.serviclick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
public class ServiClickApplication {

    public static void main(String[] args) {
        // Carga el .env si existe de forma pacífica
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        // TRUCO DE RESPALDO: Si no hay .env, le inyectamos la clave temporal por código
        if (System.getProperty("GEMINI_API_KEY") == null) {
            System.setProperty("GEMINI_API_KEY", "llave_temporal_de_prueba_12345");
        }

        SpringApplication.run(ServiClickApplication.class, args);
    }
}