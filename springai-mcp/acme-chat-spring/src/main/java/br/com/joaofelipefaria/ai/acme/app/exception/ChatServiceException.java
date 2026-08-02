package br.com.joaofelipefaria.ai.acme.app.exception;

/**
 * Thrown when communication with the underlying LLM (Ollama) fails.
 */
public class ChatServiceException extends RuntimeException {

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
