package br.com.joaofelipefaria.ai.springai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Inbound payload for the chat endpoint.
 *
 * @param question the question to be sent to the LLM
 */
public record ChatRequest(

        @NotBlank(message = "question must not be blank")
        @Size(max = 4000, message = "question must not exceed 4000 characters")
        String question

) {
}
