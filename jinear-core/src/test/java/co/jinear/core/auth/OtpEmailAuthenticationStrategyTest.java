package co.jinear.core.auth;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.account.AccountUpdateService;
import co.jinear.core.service.auth.OtpEmailAuthenticationStrategy;
import co.jinear.core.service.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OtpEmailAuthenticationStrategyTest {

    private static final String CSRF = "csrf-1";
    private static final String TOKEN_ID = "token-1";
    private static final String EMAIL = "user@example.com";
    private static final String ACCOUNT_ID = "account-1";
    private static final String REAL_CODE = "123456";
    private static final Long ATTEMPT_PENALTY_MILLIS = 10L * 60 * 1000;

    private final TokenService tokenService = Mockito.mock(TokenService.class);
    private final AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
    private final OtpEmailAuthenticationStrategy strategy = new OtpEmailAuthenticationStrategy(
            tokenService,
            accountRetrieveService,
            Mockito.mock(AccountInitializeService.class),
            Mockito.mock(AccountUpdateService.class),
            Mockito.mock(AccountRoleService.class));

    @BeforeEach
    void givenValidToken() {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenId(TOKEN_ID);
        tokenDto.setRelatedObject(EMAIL);
        tokenDto.setCommonToken(REAL_CODE);
        Mockito.when(tokenService.retrieveValidToken(CSRF, TokenType.EMAIL_LOGIN)).thenReturn(tokenDto);
    }

    @Test
    void wrongCodeIsChargedAndRejected() {
        assertThrows(NoAccessException.class, () -> strategy.auth(authVo("000000")));

        Mockito.verify(tokenService).chargeAttempt(TOKEN_ID, ATTEMPT_PENALTY_MILLIS);
        Mockito.verify(tokenService, Mockito.never()).passivizeToken(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(PassiveReason.class));
    }

    @Test
    void attemptOverTheLimitIsRejectedBeforeTheCodeIsChecked() {
        Mockito.doThrow(new NoAccessException()).when(tokenService).chargeAttempt(TOKEN_ID, ATTEMPT_PENALTY_MILLIS);

        assertThrows(NoAccessException.class, () -> strategy.auth(authVo(REAL_CODE)));

        Mockito.verify(tokenService, Mockito.never()).passivizeToken(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(PassiveReason.class));
        Mockito.verifyNoInteractions(accountRetrieveService);
    }

    @Test
    void correctCodeIsChargedThenTokenIsUsed() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(ACCOUNT_ID);
        Mockito.when(accountRetrieveService.retrieveByEmailOptional(EMAIL)).thenReturn(Optional.of(accountDto));

        strategy.auth(authVo(REAL_CODE));

        InOrder inOrder = Mockito.inOrder(tokenService);
        inOrder.verify(tokenService).chargeAttempt(TOKEN_ID, ATTEMPT_PENALTY_MILLIS);
        inOrder.verify(tokenService).passivizeToken(TOKEN_ID, ACCOUNT_ID, PassiveReason.EMAIL_LOGIN_TOKEN_USED);
    }

    private AuthVo authVo(String code) {
        AuthVo authVo = new AuthVo();
        authVo.setCsrf(CSRF);
        authVo.setCode(code);
        return authVo;
    }
}
