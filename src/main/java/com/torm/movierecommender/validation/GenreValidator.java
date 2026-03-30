package com.torm.movierecommender.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class GenreValidator implements ConstraintValidator<Genre, String> {
    private static final Set<String> ALLOWED_GENRES = Set.of(
            "action", "adventure", "comedy", "drama", "horror", "thriller",
            "romance", "sci-fi", "fantasy", "crime", "mystery", "animation"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank())
            return true;

       String cleanedValue = value.strip().toLowerCase();

        return ALLOWED_GENRES.contains(cleanedValue);
    }
}