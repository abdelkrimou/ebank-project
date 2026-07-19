package com.ebank.backend.chatbot.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmbeddingClient {

    private static final String MODEL = "text-embedding-3-small";

    private final RestClient openAiRestClient;

    @SuppressWarnings("unchecked")
    public float[] embed(String text) {
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "input", text
        );

        Map<String, Object> response = openAiRestClient.post()
                .uri("/embeddings")
                .body(body)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        List<Double> vector = (List<Double>) data.get(0).get("embedding");

        float[] result = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            result[i] = vector.get(i).floatValue();
        }
        return result;
    }
}
