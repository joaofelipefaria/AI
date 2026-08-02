package br.com.joaofelipefaria.ai.acme.app.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds the {@link ChatClient} used to talk to the LLM served by Ollama.
 * The {@link ChatClient.Builder} is auto-configured by the
 * spring-ai-starter-model-ollama dependency based on application.yml.
 */
@Configuration
public class ChatClientConfig {

    private static final String SYSTEM_PROMPT = """
            You are a helpful, concise assistant.
            Answer clearly and directly. If you are not sure about something, say so.
            """;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

}
