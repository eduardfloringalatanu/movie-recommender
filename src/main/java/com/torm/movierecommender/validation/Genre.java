package com.torm.movierecommender.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Constraint(validatedBy = GenreValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Genre {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}