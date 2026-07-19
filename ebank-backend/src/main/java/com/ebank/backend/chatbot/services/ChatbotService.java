package com.ebank.backend.chatbot.services;

import com.ebank.backend.chatbot.rag.EmbeddingClient;
import com.ebank.backend.chatbot.rag.VectorStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private static final String CHAT_MODEL = "gpt-4o-mini";
    private static final int TOP_K = 3;

    private final RestClient openAiRestClient;
    private final EmbeddingClient embeddingClient;
    private final VectorStore vectorStore;

    /**
     * Answers a question about the bank account app using retrieval-augmented
     * generation: embed the question, pull the most relevant FAQ chunks,
     * and ask the chat model to answer using only that context.
     */
    @SuppressWarnings("unchecked")
    public String ask(String question) {
        List<String> context;
        try {
            float[] queryEmbedding = embeddingClient.embed(question);
            context = vectorStore.topK(queryEmbedding, TOP_K);
        } catch (Exception e) {
            log.warn("RAG retrieval failed, falling back to no context: {}", e.getMessage());
            context = List.of();
        }

        String contextBlock = context.isEmpty()
                ? "(no matching context found in the knowledge base)"
                : String.join("\n---\n", context);

        String systemPrompt = """
                You are the E-Bank assistant, embedded in a bank account management app.
                Answer the user's question using ONLY the context below when it's relevant.
                If the context doesn't cover the question, say you don't have that information
                rather than making something up. Keep answers short and concrete.

                Context:
                %s
                """.formatted(contextBlock);

        Map<String, Object> body = Map.of(
                "model", CHAT_MODEL,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", question)
                ),
                "temperature", 0.2
        );

        Map<String, Object> response = openAiRestClient.post()
                .uri("/chat/completions")
                .body(body)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}
