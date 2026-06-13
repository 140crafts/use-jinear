package co.jinear.core.service.auth;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.auth.apple.AppleUserOperationService;
import co.jinear.core.service.client.apple.SignInWithAppleCallerService;
import co.jinear.core.service.client.apple.model.IdTokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "signinwithapple.enabled", havingValue = "true")
public class AppleAuthenticationStrategy implements AuthenticationStrategy {

    private final AppleUserOperationService appleUserOperationService;
    private final AccountRetrieveService accountRetrieveService;
    private final SignInWithAppleCallerService signInWithAppleCallerService;
    private final AccountInitializeService accountInitializeService;
    private final AccountRoleService accountRoleService;

    @Override
    public AuthResponseVo auth(AuthVo authVo) {
        IdTokenPayload idTokenPayload = signInWithAppleCallerService.appleAuth(authVo.getCode(), authVo.getWebClient());
        AccountDto accountDto = retrieveAccount(idTokenPayload, authVo.getLocale());
        Collection<GrantedAuthority> grantedAuthorities = retrieveAuthorities(accountDto.getAccountId());
        return new AuthResponseVo(accountDto.getAccountId(), authVo.getLocale(), grantedAuthorities);
    }

    @Override
    public ProviderType getType() {
        return ProviderType.SIGN_IN_WITH_APPLE;
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String accountId) {
        return accountRoleService.retrieveAccountAuthorities(accountId);
    }

    private AccountDto retrieveAccount(IdTokenPayload idTokenPayload, LocaleType locale) {
        log.info("Retrieve account has started. idTokenPayload: {}", idTokenPayload);
        return accountRetrieveService.retrieveByEmailOptional(idTokenPayload.getEmail())
                .orElseGet(() -> retrieveAccountBySub(idTokenPayload, locale));
    }

    private AccountDto retrieveAccountBySub(IdTokenPayload idTokenPayload, LocaleType locale) {
        return appleUserOperationService.retrieveAccountIdWithAppleUserId(idTokenPayload.getSub())
                .map(accountRetrieveService::retrieve)
                .orElseGet(() -> registerAccount(idTokenPayload, locale));
    }

    private AccountDto registerAccount(IdTokenPayload idTokenPayload, LocaleType locale) {
        AccountInitializeVo accountInitializeVo = mapAccountInitializeVo(idTokenPayload, locale);
        AccountDto accountDto = accountInitializeService.initializeAccount(accountInitializeVo);
        appleUserOperationService.initialize(idTokenPayload, accountDto.getAccountId());
        return accountDto;
    }

    private AccountInitializeVo mapAccountInitializeVo(IdTokenPayload idTokenPayload, LocaleType locale) {
        AccountInitializeVo accountInitializeVo = new AccountInitializeVo();
        accountInitializeVo.setEmail(idTokenPayload.getEmail());
        accountInitializeVo.setEmailConfirmed(Boolean.TRUE);
        accountInitializeVo.setPassword(UUID.randomUUID().toString());
        accountInitializeVo.setLocale(locale);
        return accountInitializeVo;
    }
}
