package procedural_generation.tasks.task2;

import java.util.Random;

public class Perlin {

    /**
     * Compute dot product between gradient vector at lattice point
     * and offset vector from lattice point to sample point.
     */
    private static float dotGridGradient(int angleDeg, int gridX, int gridY, float x, float y) {
        // Convert gradient angle to a unit vector
        float gx = (float) Math.cos(Math.toRadians(angleDeg));
        float gy = (float) Math.sin(Math.toRadians(angleDeg));

        // Offset vector from corner to point
        float dx = x - gridX;
        float dy = y - gridY;

        // Dot product
        return gx * dx + gy * dy;
    }

    /** Smoothstep curve for interpolation */
    public static float smoothstep(float t) {
        return t * t * (3 - 2 * t);
    }

    /** Linear interpolation */
    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    /**
     * Generate random gradient directions (in degrees) for each lattice point
     */
    public static int[][] directionArrayGenerate(int numCol, int numRow, int seed) {
        Random random = new Random(seed);
        int[][] array = new int[numCol][numRow];
        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numRow; j++) {
                array[i][j] = random.nextInt(360); // random angle
            }
        }
        return array;
    }

    /**
     * Compute Perlin noise value at (x, y)
     */
    public static float perlinGenerate(float x, float y, int[][] gradients) {
        // Grid cell coordinates surrounding point
        int x0 = (int) Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int) Math.floor(y);
        int y1 = y0 + 1;

        // Fractional part of (x, y)
        float sx = x - x0;
        float sy = y - y0;

        // Smooth interpolation weights
        float u = smoothstep(sx);
        float v = smoothstep(sy);

        // Dot products between gradient vector & offset vector
        float n00 = dotGridGradient(gradients[x0][y0], x0, y0, x, y);
        float n10 = dotGridGradient(gradients[x1][y0], x1, y0, x, y);
        float n01 = dotGridGradient(gradients[x0][y1], x0, y1, x, y);
        float n11 = dotGridGradient(gradients[x1][y1], x1, y1, x, y);

        // Interpolate the results
        float ix0 = lerp(n00, n10, u);
        float ix1 = lerp(n01, n11, u);
        float value = lerp(ix0, ix1, v);

        // Normalize (optional, values typically in [-√0.5, √0.5])
        return value;
    }

    /**
     * Generate a 2D array of Perlin noise values
     */
    public static float[][] perlinArrayGenerate(int numCol, int numRow, int seed, float step) {
        float[][] noise = new float[numCol][numRow];
        int[][] gradients = directionArrayGenerate(numCol + 1, numRow + 1, seed);

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numRow; j++) {
                float x = i * step;
                float y = j * step;
                noise[i][j] = perlinGenerate(x, y, gradients);
            }
        }

        return noise;
    }
}
