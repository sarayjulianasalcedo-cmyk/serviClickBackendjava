package com.sinquinto.serviclick.Chat.Application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String obtenerRespuesta(String mensajeUsuario) {

        String prompt = """
                Eres ServiBot, el asistente oficial de ServiClick.

                SOLO puedes responder preguntas relacionadas con:
                - servicios
                - vendedores
                - clientes
                - funcionamiento de la plataforma
                - publicación de servicios

                Si preguntan algo fuera de ServiClick responde:
                Solo puedo ayudar con temas relacionados con ServiClick.

                Pregunta:
                """ + mensajeUsuario;

        // ✅ MODELO CORRECTO
        String url =
                "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key="
                        + apiKey;

        String body = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                prompt
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
        );

        try {

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            String.class
                    );

            return extraerTexto(response.getBody());

        } catch (Exception e) {

            e.printStackTrace();

            return "Error al conectar con Gemini: " + e.getMessage();
        }
    }

    private String extraerTexto(String response) {

        try {

            int inicio =
                    response.indexOf("\"text\": \"") + 9;

            int fin =
                    response.indexOf("\"", inicio);

            return response.substring(inicio, fin);

        } catch (Exception e) {

            return "No pude responder en este momento";
        }
    }
}