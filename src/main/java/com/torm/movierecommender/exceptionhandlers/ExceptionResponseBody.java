package com.torm.movierecommender.exceptionhandlers;

import java.time.Instant;
import java.util.List;

record ArgumentError(
        String argument,
        String errorCode
) {}

public record ExceptionResponseBody(
        Instant timestamp,
        int status,
        String errorType,
        List<ArgumentError> argumentErrors
) {}