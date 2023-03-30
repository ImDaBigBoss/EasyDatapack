package com.github.imdabigboss.easydatapack.api.utils.math;

/**
 * Some math utilities.
 */
public class ExtraMath {
    /**
     * Finds the biggest number in the provided arguments.
     * @param a the numbers to compare
     * @return the biggest number
     */
    public static int max(int... a) {
        int max = Integer.MIN_VALUE;
        for (int i : a) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Finds the smallest number in the provided arguments.
     * @param a the numbers to compare
     * @return the smallest number
     */
    public static int min(int... a) {
        int min = Integer.MAX_VALUE;
        for (int i : a) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    /**
     * Clamp a number between two other numbers. For example if you have a number that is 10 and you want to clamp it
     * between 0 and 5, it will return 5. If you have a number that is 3 and you want to clamp it between 0 and 5, it
     * will return 3. If you have a number that is -1 and you want to clamp it between 0 and 5, it will return 0.
     * @param value the number to clamp
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped number
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }
}
