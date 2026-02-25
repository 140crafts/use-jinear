package co.jinear.core.service.auth;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.enumtype.token.TokenType;
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
public class SingleUseLoginTokenAuthenticationStrategy implements AuthenticationStrategy {

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
                .orElseThrow(BusinessException::new);
        invalidateToken(token.getTokenId(), accountId);
        updateEmailAsConfirmed(accountId);
        Collection<GrantedAuthority> grantedAuthorities = retrieveAuthorities(accountId);
        return new AuthResponseVo(accountId, authVo.getLocale(), grantedAuthorities);
    }

    @Override
    public ProviderType getType() {
        return ProviderType.SINGLE_USE_LOGIN_TOKEN;
    }

    private void validateTokensMatch(TokenDto token, AuthVo authVo) {
        if (!checkTokensMatch(token, authVo)) {
            tokenService.shortenTokenLifespan(token);
            throw new NoAccessException();
        }
    }

    private boolean checkTokensMatch(TokenDto token, AuthVo authVo) {
        String uniqueCode = authVo.getCode();
        String realCode = token.getUniqueToken();
        return realCode.equalsIgnoreCase(uniqueCode);
    }

    private TokenDto retrieveToken(AuthVo authVo) {
        String uniqueCode = authVo.getCode();
        return tokenService.retrieveValidToken(uniqueCode, TokenType.SINGLE_USE_LOGIN_TOKEN);
    }

    private void invalidateToken(String tokenId, String accountId) {
        tokenService.passivizeToken(tokenId, accountId, PassiveReason.SINGLE_LOGIN_TOKEN_USED);
    }

    private void updateEmailAsConfirmed(String accountId) {
        accountUpdateService.updateEmailConfirmed(accountId, Boolean.TRUE);
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String accountId) {
        return accountRoleService.retrieveAccountAuthorities(accountId);
    }
}
