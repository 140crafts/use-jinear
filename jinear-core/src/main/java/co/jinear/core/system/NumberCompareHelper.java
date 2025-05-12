package co.jinear.core.system;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Objects;

@UtilityClass
public class NumberCompareHelper {

    public static boolean isEquals(Long value, long number) {
        return Objects.nonNull(value) && value == number;
    }

    public static boolean isLessThanOrEqual(Long value, long number) {
        return Objects.nonNull(value) && value <= number;
    }

    public static boolean isLessThan(Long value, long number) {
        return Objects.nonNull(value) && value < number;
    }

    public static boolean isGreaterThanOrEqual(Long value, long number) {
        return Objects.nonNull(value) && value >= number;
    }

    public static boolean isGreaterThan(Long value, long number) {
        return Objects.nonNull(value) && value > number;
    }

    public static boolean isEquals(Integer value, int number) {
        return Objects.nonNull(value) && value == number;
    }

    public static boolean isLessThanOrEqual(Integer value, int number) {
        return Objects.nonNull(value) && value <= number;
    }

    public static boolean isLessThan(Integer value, int number) {
        return Objects.nonNull(value) && value < number;
    }

    public static boolean isGreaterThanOrEqual(Integer value, int number) {
        return Objects.nonNull(value) && value >= number;
    }

    public static boolean isGreaterThan(Integer value, int number) {
        return Objects.nonNull(value) && value > number;
    }

    public static boolean isEquals(BigDecimal val1, BigDecimal val2) {
        return Objects.nonNull(val1) && Objects.nonNull(val2) && val1.compareTo(val2) == 0;
    }

    public static boolean isLessThanOrEqual(BigDecimal val1, BigDecimal val2) {
        return Objects.nonNull(val1) && Objects.nonNull(val2) && val1.compareTo(val2) <= 0;
    }

    public static boolean isLessThan(BigDecimal val1, BigDecimal val2) {
        return Objects.nonNull(val1) && Objects.nonNull(val2) && val1.compareTo(val2) < 0;
    }

    public static boolean isGreaterThanOrEqual(BigDecimal val1, BigDecimal val2) {
        return Objects.nonNull(val1) && Objects.nonNull(val2) && val1.compareTo(val2) >= 0;
    }

    public static boolean isGreaterThan(BigDecimal val1, BigDecimal val2) {
        return Objects.nonNull(val1) && Objects.nonNull(val2) && val1.compareTo(val2)>0;
    }

    public static boolean isPositive(BigDecimal val) {
        return isGreaterThan(val, BigDecimal.ZERO);
    }

    public static boolean isNegative(BigDecimal val) {
        return isLessThan(val, BigDecimal.ZERO);
    }
}
