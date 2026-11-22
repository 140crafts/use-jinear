package co.jinear.core.auth;

import co.jinear.core.converter.token.TokenConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.repository.TokenRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest {

    private static final String TOKEN_ID = "token-1";
    private static final Long ATTEMPT_PENALTY_MILLIS = 10L * 60 * 1000;

    private final TokenRepository tokenRepository = Mockito.mock(TokenRepository.class);
    private final TokenService tokenService = new TokenService(
            tokenRepository,
            Mockito.mock(PassiveService.class),
            Mockito.mock(TokenConverter.class));

    @Test
    void attemptIsChargedWithTheTenMinutePenalty() {
        givenChargedTokenCount(1);

        assertDoesNotThrow(() -> tokenService.chargeAttempt(TOKEN_ID));
    }

    @Test
    void attemptOnATokenThatCannotBeChargedIsRejected() {
        givenChargedTokenCount(0);

        assertThrows(NoAccessException.class, () -> tokenService.chargeAttempt(TOKEN_ID));
    }

    private void givenChargedTokenCount(int chargedTokenCount) {
        Mockito.when(tokenRepository.chargeAttempt(
                        ArgumentMatchers.eq(TOKEN_ID),
                        ArgumentMatchers.eq(ATTEMPT_PENALTY_MILLIS),
                        ArgumentMatchers.anyLong()))
                .thenReturn(chargedTokenCount);
    }
}
