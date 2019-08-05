package com.simplejcode.commons.misc.util;

import java.math.*;

public final class DecimalUtils {

    private DecimalUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static final BigDecimal HALF = new BigDecimal("0.5");
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal HUNDRED = new BigDecimal("100");


    public static BigDecimal orNull(String s) {
        return s.isEmpty() ? null : new BigDecimal(s);
    }

    public static BigDecimal round(BigDecimal d, int maxPrecision) {
        return round(d, maxPrecision, 0, Integer.MAX_VALUE);
    }

    public static BigDecimal round(BigDecimal d, int maxPrecision, int minScale, int maxScale) {
        d = d.stripTrailingZeros();

        int newScale = maxPrecision - d.precision() + d.scale();
        newScale = Math.max(newScale, minScale);
        newScale = Math.min(newScale, maxScale);

        return setScale(d, newScale);
    }

    public static BigDecimal setScale(BigDecimal d, int newScale) {
        return d.setScale(newScale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    //-----------------------------------------------------------------------------------

    public static BigDecimal percentOf(BigDecimal amount, BigDecimal percents) {
        return amount.multiply(percents).movePointLeft(2);
    }

    public static BigDecimal inPercents(BigDecimal a, BigDecimal b) {
        return a == null || b == null || b.signum() == 0 ? BigDecimal.ZERO :
                a.divide(b, MathContext.DECIMAL64).movePointRight(2);
    }

    //-----------------------------------------------------------------------------------

    public static boolean isPositive(BigDecimal decimal) {
        return decimal.signum() > 0;
    }

    public static boolean isNegative(BigDecimal decimal) {
        return decimal.signum() < 0;
    }

}
