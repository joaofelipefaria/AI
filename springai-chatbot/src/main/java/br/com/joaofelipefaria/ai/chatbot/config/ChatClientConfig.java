package br.com.joaofelipefaria.ai.chatbot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
	
	@Bean
	public ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(repository)
				.maxMessages(20)
				.build();
	}

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder.defaultAdvisors(
        		MessageChatMemoryAdvisor.builder(chatMemory).build()
        		).build();
    }
}