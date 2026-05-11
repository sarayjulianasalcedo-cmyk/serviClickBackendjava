package com.sinquinto.serviclick.Chat.Infrastructure;

import com.sinquinto.serviclick.Chat.Application.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String respuesta = chatService.obtenerRespuesta(request.getMensaje());

        return new ChatResponse(respuesta);
    }
}