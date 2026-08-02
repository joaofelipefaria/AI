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
            You are an HR assistant.
            You must consult employee leave information and employee data through the available tools.
            You must also consult company leave policies from the RAG context.
            Do not invent or fabricate information.
            If the information is missing or unclear, say that you cannot confirm it and ask for the necessary detail.
            Answer clearly, concisely, and professionally.
            """;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

}
