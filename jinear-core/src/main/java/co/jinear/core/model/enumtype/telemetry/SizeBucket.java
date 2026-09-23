package co.jinear.core.model.enumtype.telemetry;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SizeBucket {

    ZERO(0L),
    ONE(1L),
    TWO_TO_FIVE(5L),
    SIX_TO_TWENTY_FIVE(25L),
    TWENTY_SIX_TO_HUNDRED(100L),
    HUNDRED_ONE_TO_FIVE_HUNDRED(500L),
    OVER_FIVE_HUNDRED(Long.MAX_VALUE);

    private final long upperBound;

    public static SizeBucket from(long count) {
        return Arrays.stream(values())
                .filter(sizeBucket -> count <= sizeBucket.getUpperBound())
                .findFirst()
                .orElse(OVER_FIVE_HUNDRED);
    }
}
