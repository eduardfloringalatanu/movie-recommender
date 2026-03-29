package com.torm.movierecommender.utils;

public class MathUtils {
    public static float cosineSimilarity(float[] a, float[] b) {
        int n = Math.min(a.length, b.length);

        double dotProduct = 0.0, squaredNormA= 0.0, squaredNormB = 0.0;

        for(int i = 0; i < n; ++i) {
            dotProduct += a[i] * b[i];

            squaredNormA += a[i] * a[i];
            squaredNormB += b[i] * b[i];
        }

        if (squaredNormA == 0.0 || squaredNormB == 0.0)
            return 0.0f;

        return (float)(dotProduct / Math.sqrt(squaredNormA * squaredNormB));
    }
}