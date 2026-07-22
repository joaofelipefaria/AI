package br.com.joaofelipefaria.ai.springai.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard error payload returned for any failed request.
 *
 * @param status    HTTP status code
 * @param error     short error description
 * @param messages  detailed error messages (e.g. validation failures)
 * @param path      the request path that triggered the error
 * @param timestamp when the error occurred
 */
public record ErrorResponse(

        int status,
        String error,
        List<String> messages,
        String path,
        Instant timestamp

) {

    public static ErrorResponse of(int status, String error, List<String> messages, String path) {
        return new ErrorResponse(status, error, messages, path, Instant.now());
    }

}
