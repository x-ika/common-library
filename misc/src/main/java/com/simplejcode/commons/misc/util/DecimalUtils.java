package com.simplejcode.commons.misc.util;

import java.math.*;
import java.util.Collection;
import java.util.function.Function;

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

    public static BigDecimal setScale(BigDecimal d, int maxScale) {
        return d.setScale(maxScale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal truncate(BigDecimal d, int maxScale) {
        return d.setScale(maxScale, RoundingMode.DOWN).stripTrailingZeros();
    }

    //-----------------------------------------------------------------------------------

    public static BigDecimal percentOf(BigDecimal amount, BigDecimal percents) {
        return amount.multiply(percents).movePointLeft(2);
    }

    public static BigDecimal inPercents(BigDecimal a, BigDecimal b) {
        return a == null || b == null || b.signum() == 0 ? BigDecimal.ZERO :
                a.divide(b, MathContext.DECIMAL64).movePointRight(2);
    }

    public static BigDecimal changePercent(BigDecimal prevValue, BigDecimal curValue) {
        return inPercents(curValue, prevValue).subtract(HUNDRED);
    }

    //-----------------------------------------------------------------------------------

    public static boolean isPositive(BigDecimal decimal) {
        return decimal.signum() > 0;
    }

    public static boolean isNegative(BigDecimal decimal) {
        return decimal.signum() < 0;
    }

    //-----------------------------------------------------------------------------------

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a == null ? b : b == null ? a : a.min(b);
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a == null ? b : b == null ? a : a.max(b);
    }

    public static BigDecimal sum(BigDecimal a, BigDecimal b) {
        return ObjectUtils.nvl(a, BigDecimal.ZERO).add(ObjectUtils.nvl(b, BigDecimal.ZERO));
    }

    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return ObjectUtils.nvl(a, BigDecimal.ZERO).subtract(ObjectUtils.nvl(b, BigDecimal.ZERO));
    }

    //-----------------------------------------------------------------------------------

    public static <T> BigDecimal min(Collection<T> list, Function<T, BigDecimal> mapper) {
        BigDecimal result = null;
        for (T t : list) {
            if (t != null) {
                BigDecimal cur = mapper.apply(t);
                result = result == null ? cur : result.min(cur);
            }
        }
        return result;
    }

    public static <T> BigDecimal max(Collection<T> list, Function<T, BigDecimal> mapper) {
        BigDecimal result = null;
        for (T t : list) {
            if (t != null) {
                BigDecimal cur = mapper.apply(t);
                result = result == null ? cur : result.max(cur);
            }
        }
        return result;
    }

    public static <T> BigDecimal sum(Collection<T> list, Function<T, BigDecimal> mapper) {
        BigDecimal result = BigDecimal.ZERO;
        for (T t : list) {
            if (t != null) {
                result = result.add(mapper.apply(t));
            }
        }
        return result;
    }

}
