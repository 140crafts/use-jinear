package co.jinear.core.service.auth;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.account.AccountUpdateService;
import co.jinear.core.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpEmailAuthenticationStrategy implements AuthenticationStrategy {

    private final TokenService tokenService;
    private final AccountRetrieveService accountRetrieveService;
    private final AccountInitializeService accountInitializeService;
    private final AccountUpdateService accountUpdateService;
    private final AccountRoleService accountRoleService;

    @Override
    public AuthResponseVo auth(AuthVo authVo) {
        TokenDto token = retrieveToken(authVo);
        validateTokensMatch(token, authVo);
        String accountId = Optional.of(token)
                .map(TokenDto::getRelatedObject)
                .map(this::retrieveAccountId)
                .orElseThrow(BusinessException::new);
        invalidateToken(token.getTokenId(), accountId);
        updateEmailAsConfirmed(accountId);
        Collection<GrantedAuthority> grantedAuthorities = retrieveAuthorities(accountId);
        return new AuthResponseVo(accountId, authVo.getLocale(), grantedAuthorities);
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String accountId) {
        return accountRoleService.retrieveAccountAuthorities(accountId);
    }

    private TokenDto retrieveToken(AuthVo emailLoginRequest) {
        String csrf = emailLoginRequest.getCsrf();
        return tokenService.retrieveValidToken(csrf, TokenType.EMAIL_LOGIN);
    }

    @Override
    public ProviderType getType() {
        return ProviderType.OTP_MAIL;
    }

    private void validateTokensMatch(TokenDto token, AuthVo emailAuthVo) {
        if (!checkTokensMatch(token, emailAuthVo)) {
            tokenService.shortenTokenLifespan(token);
            throw new NoAccessException();
        }
    }

    private boolean checkTokensMatch(TokenDto token, AuthVo emailAuthVo) {
        String usersInput = emailAuthVo.getCode();
        String realCode = token.getCommonToken();
        return realCode.equalsIgnoreCase(usersInput);
    }

    private String retrieveAccountId(String email) {
        return accountRetrieveService.retrieveByEmailOptional(email)
                .map(AccountDto::getAccountId)
                .orElseGet(() -> createAccountIfNotFound(email));
    }

    private String createAccountIfNotFound(String email) {
        AccountInitializeVo accountInitializeVo = new AccountInitializeVo();
        accountInitializeVo.setEmail(email);
        accountInitializeVo.setEmailConfirmed(Boolean.TRUE);
        AccountDto newAccount = accountInitializeService.initializeAccount(accountInitializeVo);
        return newAccount.getAccountId();
    }

    private void invalidateToken(String tokenId, String accountId) {
        tokenService.passivizeToken(tokenId, accountId, PassiveReason.EMAIL_LOGIN_TOKEN_USED);
    }

    private void updateEmailAsConfirmed(String accountId) {
        accountUpdateService.updateEmailConfirmed(accountId, Boolean.TRUE);
    }
}
