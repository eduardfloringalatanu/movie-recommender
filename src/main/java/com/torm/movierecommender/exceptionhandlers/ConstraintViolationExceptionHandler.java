package com.torm.movierecommender.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ConstraintViolationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseBody handleConstraintViolationException(ConstraintViolationException e) {
        List<ArgumentError> argumentErrors = e.getConstraintViolations()
                .stream()
                .map(error -> {
                    String propertyPath = error.getPropertyPath().toString();
                    String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);

                    return new ArgumentError(
                            field,
                            error.getMessage()
                    );
                })
                .toList();

        return new ExceptionResponseBody(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "ARGUMENT_INVALID_ERROR",
                argumentErrors
        );
    }
}