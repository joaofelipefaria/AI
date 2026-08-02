package br.com.joaofelipefaria.ai.acme.app.service;

import br.com.joaofelipefaria.ai.acme.app.dto.ChatRequest;
import br.com.joaofelipefaria.ai.acme.app.dto.ChatResponse;

/**
 * Contract for sending a question to the configured LLM and returning its answer.
 */
public interface ChatService {

    /**
     * Sends the given question to the LLM and returns the generated answer.
     *
     * @param request the incoming chat request containing the question
     * @return the LLM-generated answer wrapped in a {@link ChatResponse}
     */
    ChatResponse ask(ChatRequest request);

}
