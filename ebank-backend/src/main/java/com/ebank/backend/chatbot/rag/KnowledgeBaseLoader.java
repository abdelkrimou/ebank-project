package com.ebank.backend.chatbot.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads src/main/resources/knowledge-base/faq.txt, splits it into
 * paragraph-sized chunks (split on blank lines), embeds each chunk via
 * OpenAI, and stores it in the in-memory VectorStore. Runs once on
 * application startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KnowledgeBaseLoader {

    private static final String KB_PATH = "knowledge-base/faq.txt";

    private final VectorStore vectorStore;
    private final EmbeddingClient embeddingClient;

    @EventListener(ApplicationReadyEvent.class)
    public void loadKnowledgeBase() {
        try {
            List<String> chunks = readChunks();
            for (String chunk : chunks) {
                if (chunk.isBlank()) continue;
                try {
                    float[] embedding = embeddingClient.embed(chunk);
                    vectorStore.add(new DocumentChunk(chunk, embedding));
                } catch (Exception e) {
                    log.warn("Skipping chunk - embedding call failed (is OPENAI_API_KEY set?): {}", e.getMessage());
                }
            }
            log.info("Knowledge base loaded: {} chunks embedded", vectorStore.size());
        } catch (Exception e) {
            log.error("Failed to load knowledge base from {}: {}", KB_PATH, e.getMessage());
        }
    }

    private List<String> readChunks() throws Exception {
        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(KB_PATH).getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    if (current.length() > 0) {
                        chunks.add(current.toString().trim());
                        current.setLength(0);
                    }
                } else {
                    current.append(line).append("\n");
                }
            }
            if (current.length() > 0) {
                chunks.add(current.toString().trim());
            }
        }
        return chunks;
    }
}
