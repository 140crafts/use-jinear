package co.jinear.core.service.auth;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.account.AccountUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthEmailAuthenticationStrategy implements AuthenticationStrategy {

    private final AccountRetrieveService accountRetrieveService;
    private final AccountInitializeService accountInitializeService;
    private final AccountUpdateService accountUpdateService;
    private final AccountRoleService accountRoleService;

    @Override
    public AuthResponseVo auth(AuthVo authVo) {
        String accountId = Optional.of(authVo)
                .map(AuthVo::getEmail)
                .map(this::retrieveAccountId)
                .orElseThrow(BusinessException::new);
        updateEmailAsConfirmed(accountId);
        Collection<GrantedAuthority> grantedAuthorities = retrieveAuthorities(accountId);
        return new AuthResponseVo(accountId, authVo.getLocale(), grantedAuthorities);
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String accountId) {
        return accountRoleService.retrieveAccountAuthorities(accountId);
    }

    @Override
    public ProviderType getType() {
        return ProviderType.OAUTH_MAIL;
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

    private void updateEmailAsConfirmed(String accountId) {
        accountUpdateService.updateEmailConfirmed(accountId, Boolean.TRUE);
    }
}
