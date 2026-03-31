package com.torm.movierecommender.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleConstraintValidator implements ConstraintValidator<Title, String>, ConstraintViolationBuilder {
    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title == null || title.isBlank())
            return buildConstraintViolation(constraintValidatorContext, "TITLE_BLANK_ERROR");

        if (title.strip()
                .replaceAll("\\s+", " ")
                .length() > 500)
            return buildConstraintViolation(constraintValidatorContext, "TITLE_MAX_SIZE_ERROR");

        return true;
    }
}