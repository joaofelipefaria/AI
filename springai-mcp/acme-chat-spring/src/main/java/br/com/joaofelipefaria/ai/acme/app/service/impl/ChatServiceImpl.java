package br.com.joaofelipefaria.ai.acme.app.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import br.com.joaofelipefaria.ai.acme.app.dto.ChatRequest;
import br.com.joaofelipefaria.ai.acme.app.dto.ChatResponse;
import br.com.joaofelipefaria.ai.acme.app.exception.ChatServiceException;
import br.com.joaofelipefaria.ai.acme.app.service.ChatService;
import br.com.joaofelipefaria.ai.acme.app.service.HrToolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final HrToolService hrToolService;

    @Override
    public ChatResponse ask(ChatRequest request) {
        log.debug("Sending question to LLM: {}", request.question());

        try {
            String enhancedQuestion = buildEnhancedQuestion(request.question());

            String answer = chatClient.prompt()
                    .user(enhancedQuestion)
                    .call()
                    .content();

            String modelName = chatModel.getDefaultOptions().getModel();

            log.debug("Received answer from model [{}]", modelName);
            return ChatResponse.of(answer, modelName);

        } catch (Exception ex) {
            log.error("Failed to obtain a response from the LLM", ex);
            throw new ChatServiceException("Failed to obtain a response from the LLM", ex);
        }
    }

    private String buildEnhancedQuestion(String question) {
        return hrToolService.extractEmployeeId(question)
                .map(employeeId -> question + "\n\nUse the HR tools to retrieve employee " + employeeId + " data and leave records before answering.")
                .orElse(question);
    }

}
