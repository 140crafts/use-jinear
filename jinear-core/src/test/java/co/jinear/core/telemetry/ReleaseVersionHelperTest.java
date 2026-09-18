package co.jinear.core.telemetry;

import co.jinear.core.system.util.ReleaseVersionHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseVersionHelperTest {

    @Test
    void recognizesOnlyReleaseTags() {
        assertThat(ReleaseVersionHelper.isRelease("v0.1.1681")).isTrue();
        assertThat(ReleaseVersionHelper.isRelease("v1.2")).isTrue();
        assertThat(ReleaseVersionHelper.isRelease("ab5df1ab")).isFalse();
        assertThat(ReleaseVersionHelper.isRelease("dev")).isFalse();
        assertThat(ReleaseVersionHelper.isRelease("0.1.1681")).isFalse();
        assertThat(ReleaseVersionHelper.isRelease("v0.1.1681-rc1")).isFalse();
        assertThat(ReleaseVersionHelper.isRelease(null)).isFalse();
    }

    @Test
    void comparesSegmentsAsNumbers() {
        assertThat(ReleaseVersionHelper.isNewer("v0.1.1700", "v0.1.999")).isTrue();
        assertThat(ReleaseVersionHelper.isNewer("v0.1.999", "v0.1.1700")).isFalse();
        assertThat(ReleaseVersionHelper.isNewer("v0.2.0", "v0.1.9999")).isTrue();
        assertThat(ReleaseVersionHelper.isNewer("v0.1.1.1", "v0.1.1")).isTrue();
        assertThat(ReleaseVersionHelper.isNewer("v0.1.1681", "v0.1.1681")).isFalse();
    }

    @Test
    void neverCallsANonReleaseNewer() {
        assertThat(ReleaseVersionHelper.isNewer("ab5df1ab", "v0.1.1")).isFalse();
        assertThat(ReleaseVersionHelper.isNewer("v0.1.1", "dev")).isFalse();
        assertThat(ReleaseVersionHelper.isNewer(null, "v0.1.1")).isFalse();
    }
}
