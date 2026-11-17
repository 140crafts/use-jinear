package co.jinear.core.service.account;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.vo.mail.LoginMailVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.system.RandomHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static co.jinear.core.model.enumtype.token.TokenType.CONFIRM_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountMailConfirmationService {

    private static final Long EMAIL_CONFIRM_TTL = 30L * 60 * 1000;
    private static final Long ATTEMPT_PENALTY_MILLIS = 10L * 60 * 1000;

    private final AccountRetrieveService accountRetrieveService;
    private final AccountUpdateService accountUpdateService;
    private final TokenService tokenService;
    private final MailService mailService;
    private final AccountLockService accountLockService;

    public void resendConfirmEmailMail(String email, LocaleType preferredLocale) {
        log.info("Resend confirm email mail. email: {}, preferredLocale: {}", email, preferredLocale);
        AccountDto accountDto = accountRetrieveService.retrieveByEmailOptional(email).orElseThrow(NotFoundException::new);
        sendConfirmEmailMail(accountDto.getAccountId(), preferredLocale);
    }

    public void sendConfirmEmailMail(String accountId, LocaleType preferredLocale) {
        log.info("Send confirm email mail has started for accountId: {}, preferredLocale: {}", accountId, preferredLocale);
        AccountDto accountDto = accountRetrieveService.retrieve(accountId);
        validateAccountMailNotConfirmed(accountDto);
        accountLockService.startConfirmEmailMailCooldown(accountId);
        retrieveActiveConfirmEmailToken(accountId).ifPresent(this::passivizeToken);
        TokenDto tokenDto = generateConfirmEmailToken(accountDto);
        sendConfirmationMail(preferredLocale, accountDto, tokenDto);
        log.info("Send confirm email mail has finished for accountId: {}", accountId);
    }

    public void confirmEmail(String email, String code) {
        log.info("Confirm email has started for email: {}", email);
        TokenDto tokenDto = accountRetrieveService.retrieveByEmailOptional(email)
                .map(AccountDto::getAccountId)
                .flatMap(this::retrieveActiveConfirmEmailToken)
                .orElseThrow(this::invalidCodeException);
        tokenService.chargeAttempt(tokenDto.getTokenId(), ATTEMPT_PENALTY_MILLIS);
        validateCodesMatch(tokenDto, code);
        accountUpdateService.updateEmailConfirmed(tokenDto.getRelatedObject(), Boolean.TRUE);
        passivizeToken(tokenDto);
        log.info("Confirm email has finished for accountId: {}", tokenDto.getRelatedObject());
    }

    private Optional<TokenDto> retrieveActiveConfirmEmailToken(String accountId) {
        return tokenService.retrieveValidTokenWithRelatedObject(accountId, CONFIRM_EMAIL);
    }

    private void passivizeToken(TokenDto tokenDto) {
        tokenService.passivizeToken(tokenDto.getTokenId(), tokenDto.getRelatedObject(), PassiveReason.USER_ACTION);
    }

    private void validateCodesMatch(TokenDto tokenDto, String code) {
        String realCode = tokenDto.getCommonToken();
        if (!realCode.equals(code)) {
            throw invalidCodeException();
        }
    }

    private BusinessException invalidCodeException() {
        return new BusinessException("account.confirm-email-code-invalid");
    }

    private void sendConfirmationMail(LocaleType preferredLocale, AccountDto accountDto, TokenDto tokenDto) {
        try {
            mailService.sendMailConfirmationMail(new LoginMailVo(accountDto.getEmail(), preferredLocale, tokenDto.getCommonToken()));
        } catch (Exception e) {
            log.error("Send confirmation mail has failed.", e);
        }
    }

    private TokenDto generateConfirmEmailToken(AccountDto accountDto) {
        log.info("Generate confirm email token has started. email: {}, accountId: {}", accountDto.getEmail(), accountDto.getAccountId());
        GenerateTokenVo vo = GenerateTokenVo.builder()
                .relatedObject(accountDto.getAccountId())
                .tokenType(CONFIRM_EMAIL)
                .uniqueToken(UUID.randomUUID().toString())
                .commonToken(RandomHelper.generateEmailCode())
                .ttl(EMAIL_CONFIRM_TTL)
                .build();
        return tokenService.generateToken(vo);
    }

    private void validateAccountMailNotConfirmed(AccountDto accountDto) {
        Optional.of(accountDto).map(AccountDto::getEmailConfirmed).filter(Boolean.TRUE::equals).ifPresent(confirmed -> {
            throw new BusinessException("account.email-already-confirmed");
        });
    }
}
