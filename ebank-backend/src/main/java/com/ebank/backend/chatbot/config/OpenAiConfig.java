package com.ebank.backend.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAiConfig {

    /**
     * Read from the OPENAI_API_KEY environment variable via
     * application.properties (openai.api.key=${OPENAI_API_KEY:}).
     * Never hardcode a real key here or anywhere else in the codebase.
     */
    @Value("${openai.api.key:}")
    private String openAiApiKey;

    @Bean
    public RestClient openAiRestClient() {
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            System.err.println("WARNING: OPENAI_API_KEY is not set. The chatbot endpoints will fail until it is. " +
                    "Set it as an environment variable before starting the app.");
        }
        return RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
