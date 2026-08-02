package br.com.joaofelipefaria.ai.acme.app.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import br.com.joaofelipefaria.ai.acme.app.dto.ChatRequest;
import br.com.joaofelipefaria.ai.acme.app.dto.ChatResponse;
import br.com.joaofelipefaria.ai.acme.app.exception.ChatServiceException;
import br.com.joaofelipefaria.ai.acme.app.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final ChatModel chatModel;

    @Override
    public ChatResponse ask(ChatRequest request) {
        log.debug("Sending question to LLM: {}", request.question());

        try {
            String answer = chatClient.prompt()
                    .user(request.question())
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

}
