package com.torm.movierecommender.validation;

import jakarta.validation.GroupSequence;

public class ValidationGroupSequences {
    public interface First {}
    public interface Second {}
    public interface Third {}
    public interface Fourth {}
    public interface Fifth {}
    public interface Sixth {}
    public interface Seventh {}
    public interface Eighth {}

    @GroupSequence({First.class, Second.class, Third.class, Fourth.class, Fifth.class, Sixth.class, Seventh.class, Eighth.class})
    public interface ValidationGroupSequence1 {}
}