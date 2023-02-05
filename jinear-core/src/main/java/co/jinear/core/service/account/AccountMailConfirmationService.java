package co.jinear.core.service.account;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.vo.mail.AccountEngageMailVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static co.jinear.core.model.enumtype.token.TokenType.CONFIRM_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountMailConfirmationService {

    private static final Long EMAIL_CONFIRM_TTL = (long) (1000 * 60 * 60 * 24 * 7);

    private final AccountRetrieveService accountRetrieveService;
    private final AccountUpdateService accountUpdateService;
    private final TokenService tokenService;
    private final MailService mailService;

    public void resendConfirmEmailMail(String email, LocaleType preferredLocale) {
        log.info("Resend confirm email mail. email: {}, preferredLocale: {}", email, preferredLocale);
        AccountDto accountDto = accountRetrieveService.retrieveByEmail(email).orElseThrow(NotFoundException::new);
        validateAccountMailNotConfirmed(accountDto);
        sendConfirmEmailMail(accountDto.getAccountId(), preferredLocale);
    }

    public void sendConfirmEmailMail(String accountId, LocaleType preferredLocale) {
        log.info("Send confirm email mail has started for accountId: {}, preferredLocale: {}", accountId, preferredLocale);
        AccountDto accountDto = accountRetrieveService.retrieve(accountId);
        validateAccountMailNotConfirmed(accountDto);
        TokenDto tokenDto = tokenService.retrieveValidTokenWithRelatedObject(accountDto.getEmail(), CONFIRM_EMAIL)
                .orElseGet(() -> generateConfirmEmailToken(accountDto));
        sendConfirmationMail(preferredLocale, accountDto, tokenDto);
        log.info("Send confirm email mail has finished for accountId: {}", accountId);
    }

    @Transactional
    public void confirmEmail(String uniqueToken) {
        log.info("Confirm email has started for uniqueToken: {}", uniqueToken);
        TokenDto tokenDto = tokenService.retrieveValidToken(uniqueToken, CONFIRM_EMAIL);
        String accountId = tokenDto.getRelatedObject();
        accountUpdateService.updateEmailConfirmed(accountId, Boolean.TRUE);
        tokenService.passivizeToken(tokenDto.getTokenId(), accountId, PassiveReason.USER_ACTION);
    }

    private void sendConfirmationMail(LocaleType preferredLocale, AccountDto accountDto, TokenDto tokenDto) {
        try {
            mailService.sendMailConfirmationMail(new AccountEngageMailVo(accountDto.getEmail(), preferredLocale, tokenDto.getUniqueToken()));
        } catch (Exception e) {
            log.error("Send confirmation mail has failed.", e);
        }
    }

    private TokenDto generateConfirmEmailToken(AccountDto accountDto) {
        log.info("Generate confirm email token has started. email: {}, accountId: {}", accountDto.getEmail(), accountDto.getAccountId());
        String token = NormalizeHelper.normalizeStrictly(UUID.randomUUID().toString());
        GenerateTokenVo vo = GenerateTokenVo.builder()
                .relatedObject(accountDto.getAccountId())
                .tokenType(CONFIRM_EMAIL)
                .uniqueToken(token)
                .commonToken(token)
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
