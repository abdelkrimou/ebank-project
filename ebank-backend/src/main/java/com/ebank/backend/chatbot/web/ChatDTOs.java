package com.ebank.backend.chatbot.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChatRequestDTO {
    private String question;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChatResponseDTO {
    private String answer;
}
