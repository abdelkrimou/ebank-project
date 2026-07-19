package com.ebank.backend.chatbot.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Minimal in-memory vector store. Fine for a small, static FAQ knowledge
 * base like this project's; swap for a real vector DB (pgvector, Pinecone,
 * Qdrant...) if the knowledge base grows or needs to persist/scale.
 */
@Component
public class VectorStore {

    private final List<DocumentChunk> chunks = new ArrayList<>();

    public void add(DocumentChunk chunk) {
        chunks.add(chunk);
    }

    public int size() {
        return chunks.size();
    }

    public List<String> topK(float[] queryEmbedding, int k) {
        return chunks.stream()
                .sorted(Comparator.comparingDouble((DocumentChunk c) -> cosineSimilarity(c.embedding(), queryEmbedding)).reversed())
                .limit(k)
                .map(DocumentChunk::text)
                .collect(Collectors.toList());
    }

    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
