package co.jinear.core.auth;

import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.auth.BadCredentialsException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountPasswordService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.auth.EmailPasswordAuthenticationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailPasswordAuthenticationStrategyTest {

    private static final String EMAIL = "user@example.com";
    private static final String ACCOUNT_ID = "account-1";
    private static final String PASSWORD = "password-1";

    private final AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
    private final AccountPasswordService accountPasswordService = Mockito.mock(AccountPasswordService.class);
    private final AccountRoleService accountRoleService = Mockito.mock(AccountRoleService.class);
    private final MailProperties mailProperties = Mockito.mock(MailProperties.class);
    private final EmailPasswordAuthenticationStrategy strategy = new EmailPasswordAuthenticationStrategy(
            accountRetrieveService,
            accountPasswordService,
            accountRoleService,
            mailProperties);

    @BeforeEach
    void givenMailIsConfigured() {
        Mockito.when(mailProperties.isMailConfigured()).thenReturn(true);
    }

    @Test
    void unconfirmedEmailIsRejectedAfterACorrectPassword() {
        givenAccount(Boolean.FALSE);

        BusinessException exception = assertThrows(BusinessException.class, () -> strategy.auth(authVo()));

        assertEquals("account.email-not-confirmed", exception.getMessage());
        Mockito.verifyNoInteractions(accountRoleService);
    }

    @Test
    void nullEmailConfirmedIsTreatedAsUnconfirmed() {
        givenAccount(null);

        assertThrows(BusinessException.class, () -> strategy.auth(authVo()));

        Mockito.verifyNoInteractions(accountRoleService);
    }

    @Test
    void unconfirmedEmailLogsInWhenMailIsNotConfigured() {
        Mockito.when(mailProperties.isMailConfigured()).thenReturn(false);
        givenAccount(Boolean.FALSE);

        AuthResponseVo authResponseVo = strategy.auth(authVo());

        assertEquals(ACCOUNT_ID, authResponseVo.getAccountId());
    }

    @Test
    void wrongPasswordIsRejectedBeforeTheConfirmationIsChecked() {
        givenAccount(Boolean.FALSE);
        Mockito.doThrow(new BadCredentialsException()).when(accountPasswordService).validatePassword(ACCOUNT_ID, PASSWORD);

        assertThrows(BadCredentialsException.class, () -> strategy.auth(authVo()));
    }

    @Test
    void confirmedEmailLogsIn() {
        givenAccount(Boolean.TRUE);

        AuthResponseVo authResponseVo = strategy.auth(authVo());

        assertEquals(ACCOUNT_ID, authResponseVo.getAccountId());
    }

    private void givenAccount(Boolean emailConfirmed) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(ACCOUNT_ID);
        accountDto.setEmail(EMAIL);
        accountDto.setEmailConfirmed(emailConfirmed);
        Mockito.when(accountRetrieveService.retrieveByEmailOptional(EMAIL)).thenReturn(Optional.of(accountDto));
    }

    private AuthVo authVo() {
        AuthVo authVo = new AuthVo();
        authVo.setEmail(EMAIL);
        authVo.setCode(PASSWORD);
        return authVo;
    }
}
