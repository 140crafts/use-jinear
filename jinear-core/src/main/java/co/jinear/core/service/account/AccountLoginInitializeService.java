package co.jinear.core.service.account;

import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.model.vo.mail.LoginMailVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.system.RandomHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static co.jinear.core.model.enumtype.token.TokenType.EMAIL_LOGIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLoginInitializeService {
    private static final Long EMAIL_LOGIN_TTL = (long) (1000 * 60 * 30);

    private final TokenService tokenService;
    private final MailService mailService;

    public AuthVo emailLoginTokenRequest(AuthVo authVo) {
        log.info("Email login token request started for:{}", authVo);
        return tokenService.retrieveValidTokenWithRelatedObject(authVo.getEmail(), EMAIL_LOGIN)
                .map(tokenDTO -> fillRequestWithExistingTokenCsrf(authVo, tokenDTO))
                .orElseGet(() -> fillNewLoginRequest(authVo));
    }

    private AuthVo fillRequestWithExistingTokenCsrf(AuthVo authVo, TokenDto tokenDto) {
        String csrf = tokenDto.getUniqueToken();
        authVo.setCsrf(csrf);
        log.info("Existing token found returning authVo: {}", authVo);
        return authVo;
    }

    private AuthVo fillNewLoginRequest(AuthVo authVo) {
        String emailCode = RandomHelper.generateEmailCode();
        authVo.setCsrf(UUID.randomUUID().toString());
        generateLoginToken(authVo, emailCode, EMAIL_LOGIN);
        sendTokenMail(authVo, emailCode);
        log.info("Email login token request completed for authVo:{}", authVo);
        return authVo;
    }

    private void generateLoginToken(AuthVo authVo, String userCredential, TokenType tokenType) {
        log.info("Creating token for {} with csrf:{} ", authVo.getEmail(), authVo.getCsrf());
        GenerateTokenVo vo = GenerateTokenVo.builder()
                .relatedObject(authVo.getEmail())
                .tokenType(tokenType)
                .uniqueToken(authVo.getCsrf())
                .commonToken(userCredential)
                .ttl(EMAIL_LOGIN_TTL)
                .build();
        tokenService.generateToken(vo);
    }

    private void sendTokenMail(AuthVo authVo, String emailCode) {
        try {
            log.info("Sending login email to:{} with preferredLocale:{} ", authVo.getEmail(), authVo.getPreferredLocale());
            mailService.sendLoginMail(new LoginMailVo(authVo.getEmail(), authVo.getPreferredLocale(), emailCode));
        } catch (Exception e) {
            log.error("Failed to send login email.", e);
        }
    }
}
