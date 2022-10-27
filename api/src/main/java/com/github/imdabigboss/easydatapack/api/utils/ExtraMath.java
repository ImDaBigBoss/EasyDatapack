package com.github.imdabigboss.easydatapack.api.utils;

public class ExtraMath {
    public static int max(int... a) {
        int max = Integer.MIN_VALUE;
        for (int i : a) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    public static int min(int... a) {
        int min = Integer.MAX_VALUE;
        for (int i : a) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }
}
