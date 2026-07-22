package br.com.joaofelipefaria.ai.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds the {@link ChatClient} used to talk to the LLM served by Ollama.
 * The {@link ChatClient.Builder} is auto-configured by the
 * spring-ai-starter-model-ollama dependency based on application.yml.
 * <p>
 * A {@link QuestionAnswerAdvisor} is attached so that, for every question,
 * the most relevant chunks stored in the pgvector store (populated from the
 * "doc-rag" folder) are retrieved and injected into the prompt as CONTEXT
 * before the LLM answers - prioritizing that content over the model's own
 * base knowledge.
 * <p>
 * A {@link SimpleLoggerAdvisor} is attached after it purely for debugging:
 * it logs the full request/response (including the CONTEXT block injected
 * by the QuestionAnswerAdvisor) so you can see exactly what was sent to the
 * model. Enable it with:
 * logging.level.org.springframework.ai.chat.client.advisor=DEBUG
 */
@Configuration
public class ChatClientConfig {

    private static final String SYSTEM_PROMPT = """
            You are a helpful, concise assistant.

            You will often receive a CONTEXT section retrieved from the
            company's own documents. Treat that CONTEXT as the primary
            source of truth:
            - If the CONTEXT answers the question, base your answer on it,
              even if it conflicts with what you already know.
            - If the CONTEXT is empty or does not cover the question, say so
              explicitly, then answer using your general knowledge.
            - Never silently blend an outdated general-knowledge answer with
              the CONTEXT; flag any difference.

            Answer clearly and directly. If you are not sure about something, say so.
            """;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .topK(4)
                        .similarityThreshold(0.0)
                        .build())
                .build();

        return chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(questionAnswerAdvisor, new SimpleLoggerAdvisor())
                .build();
    }

}