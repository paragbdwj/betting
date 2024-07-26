package com.elephants.betting.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {
    public double roundOffToTwoDecimalPlaces(double x) {
        return (double) Math.round(x * 100) /100;
    }
}
