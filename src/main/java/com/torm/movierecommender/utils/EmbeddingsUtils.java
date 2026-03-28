package com.torm.movierecommender.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmbeddingsUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String embeddingsToJson(float[] embeddings) {
        try {
            return OBJECT_MAPPER.writeValueAsString(embeddings);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize embeddings array to JSON string.", e);
        }
    }

    public static float[] jsonToEmbeddings(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, float[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON string to embeddings array.", e);
        }
    }
}