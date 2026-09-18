package co.jinear.core.oauth;

import co.jinear.core.service.oauth.provider.PkceValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PkceValidatorTest {

    private final PkceValidator validator = new PkceValidator();

    @Test
    void acceptsOnlyS256() {
        assertThat(validator.isSupportedMethod("S256")).isTrue();
        assertThat(validator.isSupportedMethod("plain")).isFalse();
        assertThat(validator.isSupportedMethod(null)).isFalse();
    }

    @Test
    void verifiesAMatchingVerifier() {
        String verifier = "a".repeat(64);
        assertThat(validator.verify(verifier, validator.derive(verifier))).isTrue();
    }

    @Test
    void rejectsAMismatchedVerifier() {
        String verifier = "a".repeat(64);
        String otherChallenge = validator.derive("b".repeat(64));
        assertThat(validator.verify(verifier, otherChallenge)).isFalse();
    }

    @Test
    void rejectsAVerifierOutsideTheLengthBounds() {
        String tooShort = "a".repeat(42);
        String tooLong = "a".repeat(129);
        assertThat(validator.verify(tooShort, validator.derive(tooShort))).isFalse();
        assertThat(validator.verify(tooLong, validator.derive(tooLong))).isFalse();
    }

    @Test
    void producesTheChallengeFromTheRfcExample() {
        assertThat(validator.derive("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"))
                .isEqualTo("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM");
    }

    @Test
    void rejectsNulls() {
        assertThat(validator.verify(null, "challenge")).isFalse();
        assertThat(validator.verify("a".repeat(64), null)).isFalse();
    }
}
