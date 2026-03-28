package com.torm.movierecommender.exceptionhandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;

@RestControllerAdvice
public class ResponseStatusExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponseBody> handleResponseStatusException(ResponseStatusException e) {
        ExceptionResponseBody response = new ExceptionResponseBody(
                Instant.now(),
                e.getStatusCode().value(),
                e.getReason(),
                null
        );

        return new ResponseEntity<>(response, e.getStatusCode());
    }
}