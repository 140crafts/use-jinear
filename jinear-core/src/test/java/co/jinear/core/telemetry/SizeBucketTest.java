package co.jinear.core.telemetry;

import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SizeBucketTest {

    @Test
    void placesEveryBoundaryInTheRightBucket() {
        assertThat(SizeBucket.from(-1)).isEqualTo(SizeBucket.ZERO);
        assertThat(SizeBucket.from(0)).isEqualTo(SizeBucket.ZERO);
        assertThat(SizeBucket.from(1)).isEqualTo(SizeBucket.ONE);
        assertThat(SizeBucket.from(2)).isEqualTo(SizeBucket.TWO_TO_FIVE);
        assertThat(SizeBucket.from(5)).isEqualTo(SizeBucket.TWO_TO_FIVE);
        assertThat(SizeBucket.from(6)).isEqualTo(SizeBucket.SIX_TO_TWENTY_FIVE);
        assertThat(SizeBucket.from(25)).isEqualTo(SizeBucket.SIX_TO_TWENTY_FIVE);
        assertThat(SizeBucket.from(26)).isEqualTo(SizeBucket.TWENTY_SIX_TO_HUNDRED);
        assertThat(SizeBucket.from(100)).isEqualTo(SizeBucket.TWENTY_SIX_TO_HUNDRED);
        assertThat(SizeBucket.from(101)).isEqualTo(SizeBucket.HUNDRED_ONE_TO_FIVE_HUNDRED);
        assertThat(SizeBucket.from(500)).isEqualTo(SizeBucket.HUNDRED_ONE_TO_FIVE_HUNDRED);
        assertThat(SizeBucket.from(501)).isEqualTo(SizeBucket.OVER_FIVE_HUNDRED);
        assertThat(SizeBucket.from(Long.MAX_VALUE)).isEqualTo(SizeBucket.OVER_FIVE_HUNDRED);
    }
}
