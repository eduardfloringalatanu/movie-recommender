package com.torm.movierecommender.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class ConstraintViolationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseBody handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        return new ExceptionResponseBody(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                message,
                null
        );
    }
}