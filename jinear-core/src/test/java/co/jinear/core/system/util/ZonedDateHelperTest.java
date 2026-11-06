package co.jinear.core.system.util;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ZonedDateHelperTest {

    @Test
    void aPreciseDateIsWrittenInUtcWhateverZoneItWasReadIn() {
        ZonedDateTime istanbul = ZonedDateTime.parse("2026-11-11T18:00:00+03:00[Europe/Istanbul]");

        assertThat(ZonedDateHelper.formatWithDateTimeFormat5(istanbul)).isEqualTo("2026-11-11T15:00:00Z");
    }

    @Test
    void aUtcDateIsWrittenUnchanged() {
        ZonedDateTime utc = ZonedDateTime.parse("2026-11-11T15:00:00Z");

        assertThat(ZonedDateHelper.formatWithDateTimeFormat5(utc)).isEqualTo("2026-11-11T15:00:00Z");
    }
}
