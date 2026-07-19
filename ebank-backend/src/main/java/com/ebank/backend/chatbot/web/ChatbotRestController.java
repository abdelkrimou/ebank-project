package com.ebank.backend.chatbot.web;

import com.ebank.backend.chatbot.services.ChatbotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "RAG-based assistant, usable from the Angular app and this REST endpoint (Telegram uses the same service)")
public class ChatbotRestController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ChatResponseDTO ask(@RequestBody ChatRequestDTO request) {
        String answer = chatbotService.ask(request.getQuestion());
        return new ChatResponseDTO(answer);
    }
}
