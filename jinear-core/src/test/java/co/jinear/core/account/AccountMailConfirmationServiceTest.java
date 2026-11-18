package co.jinear.core.account;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.auth.BadCredentialsException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.mail.LoginMailVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.service.account.AccountLockService;
import co.jinear.core.service.account.AccountMailConfirmationService;
import co.jinear.core.service.account.AccountPasswordService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountUpdateService;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountMailConfirmationServiceTest {

    private static final String EMAIL = "user@example.com";
    private static final String ACCOUNT_ID = "account-1";
    private static final String TOKEN_ID = "token-1";
    private static final String CODE = "123456";
    private static final String PASSWORD = "password-1";
    private static final String INVALID_CODE_KEY = "account.confirm-email-code-invalid";

    private final AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
    private final AccountUpdateService accountUpdateService = Mockito.mock(AccountUpdateService.class);
    private final TokenService tokenService = Mockito.mock(TokenService.class);
    private final MailService mailService = Mockito.mock(MailService.class);
    private final AccountLockService accountLockService = Mockito.mock(AccountLockService.class);
    private final AccountPasswordService accountPasswordService = Mockito.mock(AccountPasswordService.class);
    private final AccountMailConfirmationService service = new AccountMailConfirmationService(
            accountRetrieveService,
            accountUpdateService,
            tokenService,
            mailService,
            accountLockService,
            accountPasswordService);

    @Test
    void mailWithALiveCodeSendsTheSameCodeAgain() throws Exception {
        givenUnconfirmedAccount();
        givenActiveToken(TOKEN_ID);

        service.sendConfirmEmailMail(ACCOUNT_ID, LocaleType.EN);

        Mockito.verify(accountLockService).startConfirmEmailMailCooldown(ACCOUNT_ID);
        Mockito.verify(tokenService, Mockito.never()).generateToken(ArgumentMatchers.any(GenerateTokenVo.class));
        Mockito.verify(tokenService, Mockito.never()).passivizeToken(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(PassiveReason.class));
        ArgumentCaptor<LoginMailVo> mailCaptor = ArgumentCaptor.forClass(LoginMailVo.class);
        Mockito.verify(mailService).sendMailConfirmationMail(mailCaptor.capture());
        assertEquals(CODE, mailCaptor.getValue().getEmailCode());
    }

    @Test
    void mailWithoutALiveCodeMakesANewSixDigitCode() throws Exception {
        givenUnconfirmedAccount();
        Mockito.when(tokenService.retrieveValidTokenWithRelatedObject(ACCOUNT_ID, TokenType.CONFIRM_EMAIL)).thenReturn(Optional.empty());
        Mockito.when(tokenService.generateToken(ArgumentMatchers.any(GenerateTokenVo.class))).thenReturn(tokenDto(TOKEN_ID));

        service.sendConfirmEmailMail(ACCOUNT_ID, LocaleType.EN);

        InOrder inOrder = Mockito.inOrder(accountLockService, tokenService);
        inOrder.verify(accountLockService).startConfirmEmailMailCooldown(ACCOUNT_ID);
        ArgumentCaptor<GenerateTokenVo> tokenCaptor = ArgumentCaptor.forClass(GenerateTokenVo.class);
        inOrder.verify(tokenService).generateToken(tokenCaptor.capture());
        assertEquals(ACCOUNT_ID, tokenCaptor.getValue().getRelatedObject());
        assertEquals(TokenType.CONFIRM_EMAIL, tokenCaptor.getValue().getTokenType());
        assertTrue(tokenCaptor.getValue().getCommonToken().matches("\\d{6}"));
        ArgumentCaptor<LoginMailVo> mailCaptor = ArgumentCaptor.forClass(LoginMailVo.class);
        Mockito.verify(mailService).sendMailConfirmationMail(mailCaptor.capture());
        assertEquals(EMAIL, mailCaptor.getValue().getEmail());
        assertEquals(CODE, mailCaptor.getValue().getEmailCode());
    }

    @Test
    void mailInCooldownMakesNoCodeAndSendsNoMail() {
        givenUnconfirmedAccount();
        Mockito.doThrow(new BusinessException("account.confirm-email-mail-cooldown"))
                .when(accountLockService).startConfirmEmailMailCooldown(ACCOUNT_ID);

        assertThrows(BusinessException.class, () -> service.sendConfirmEmailMail(ACCOUNT_ID, LocaleType.EN));

        Mockito.verifyNoInteractions(tokenService, mailService);
    }

    @Test
    void correctCodeIsChargedThenConfirmsTheEmailAndUsesTheCode() {
        givenAccountFoundByEmail();
        givenActiveToken(TOKEN_ID);

        service.confirmEmail(EMAIL, PASSWORD, CODE);

        InOrder inOrder = Mockito.inOrder(tokenService, accountUpdateService);
        inOrder.verify(tokenService).chargeAttempt(TOKEN_ID);
        inOrder.verify(accountUpdateService).updateEmailConfirmed(ACCOUNT_ID, Boolean.TRUE);
        inOrder.verify(tokenService).passivizeToken(TOKEN_ID, ACCOUNT_ID, PassiveReason.USER_ACTION);
    }

    @Test
    void wrongCodeIsChargedAndRejected() {
        givenAccountFoundByEmail();
        givenActiveToken(TOKEN_ID);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.confirmEmail(EMAIL, PASSWORD, "000000"));

        assertEquals(INVALID_CODE_KEY, exception.getMessage());
        Mockito.verify(tokenService).chargeAttempt(TOKEN_ID);
        Mockito.verifyNoInteractions(accountUpdateService);
    }

    @Test
    void wrongPasswordGetsTheSameErrorAsAWrongCodeBeforeAnAttemptIsCharged() {
        givenAccountFoundByEmail();
        Mockito.doThrow(new BadCredentialsException()).when(accountPasswordService).validatePassword(ACCOUNT_ID, PASSWORD);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.confirmEmail(EMAIL, PASSWORD, CODE));

        assertEquals(INVALID_CODE_KEY, exception.getMessage());
        Mockito.verifyNoInteractions(tokenService, accountUpdateService);
    }

    @Test
    void unknownEmailGetsTheSameErrorAsAWrongCode() {
        Mockito.when(accountRetrieveService.retrieveByEmailOptional(EMAIL)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> service.confirmEmail(EMAIL, PASSWORD, CODE));

        assertEquals(INVALID_CODE_KEY, exception.getMessage());
        Mockito.verifyNoInteractions(tokenService, accountUpdateService);
    }

    @Test
    void accountWithoutAnActiveCodeIsRejected() {
        givenAccountFoundByEmail();
        Mockito.when(tokenService.retrieveValidTokenWithRelatedObject(ACCOUNT_ID, TokenType.CONFIRM_EMAIL)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> service.confirmEmail(EMAIL, PASSWORD, CODE));

        assertEquals(INVALID_CODE_KEY, exception.getMessage());
        Mockito.verifyNoInteractions(accountUpdateService);
    }

    private void givenUnconfirmedAccount() {
        Mockito.when(accountRetrieveService.retrieve(ACCOUNT_ID)).thenReturn(accountDto());
    }

    private void givenAccountFoundByEmail() {
        Mockito.when(accountRetrieveService.retrieveByEmailOptional(EMAIL)).thenReturn(Optional.of(accountDto()));
    }

    private void givenActiveToken(String tokenId) {
        Mockito.when(tokenService.retrieveValidTokenWithRelatedObject(ACCOUNT_ID, TokenType.CONFIRM_EMAIL)).thenReturn(Optional.of(tokenDto(tokenId)));
    }

    private AccountDto accountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(ACCOUNT_ID);
        accountDto.setEmail(EMAIL);
        accountDto.setEmailConfirmed(Boolean.FALSE);
        return accountDto;
    }

    private TokenDto tokenDto(String tokenId) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenId(tokenId);
        tokenDto.setRelatedObject(ACCOUNT_ID);
        tokenDto.setCommonToken(CODE);
        return tokenDto;
    }
}
