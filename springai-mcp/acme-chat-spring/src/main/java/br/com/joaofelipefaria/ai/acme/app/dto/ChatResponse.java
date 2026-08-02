package br.com.joaofelipefaria.ai.acme.app.dto;

import java.time.Instant;

/**
 * Outbound payload returned by the chat endpoint.
 *
 * @param answer    the LLM-generated answer
 * @param model     the model that produced the answer
 * @param timestamp when the answer was generated
 */
public record ChatResponse(

        String answer,
        String model,
        Instant timestamp

) {

    public static ChatResponse of(String answer, String model) {
        return new ChatResponse(answer, model, Instant.now());
    }

}
