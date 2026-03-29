package com.torm.movierecommender.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseBody handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ArgumentError> argumentErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        new ArgumentError(
                                error.getField(),
                                error.getDefaultMessage()
                        ))
                .toList();

        return new ExceptionResponseBody(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "ARGUMENT_INVALID_ERROR",
                argumentErrors
        );
    }
}