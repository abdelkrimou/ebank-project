package com.ebank.backend.chatbot.rag;

public record DocumentChunk(String text, float[] embedding) {
}
