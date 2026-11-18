package co.jinear.core.service.auth;

import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.account.AccountPasswordService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailPasswordAuthenticationStrategy implements AuthenticationStrategy {

    private final AccountRetrieveService accountRetrieveService;
    private final AccountPasswordService accountPasswordService;
    private final AccountRoleService accountRoleService;
    private final MailProperties mailProperties;

    @Override
    public AuthResponseVo auth(AuthVo authVo) {
        AccountDto accountDto = retrieveAccount(authVo);
        validatePassword(authVo, accountDto);
        validateEmailConfirmed(accountDto);
        Collection<GrantedAuthority> grantedAuthorities = retrieveAuthorities(accountDto.getAccountId());
        return new AuthResponseVo(accountDto.getAccountId(), authVo.getLocale(), grantedAuthorities);
    }

    @Override
    public ProviderType getType() {
        return ProviderType.PASSWORD_MAIL;
    }

    private AccountDto retrieveAccount(AuthVo authVo) {
        return accountRetrieveService.retrieveByEmailOptional(authVo.getEmail())
                .orElseThrow(NotFoundException::new);
    }

    private void validatePassword(AuthVo authVo, AccountDto accountDto) {
        accountPasswordService.validatePassword(accountDto.getAccountId(), authVo.getCode());
    }

    private void validateEmailConfirmed(AccountDto accountDto) {
        boolean emailConfirmed = Boolean.TRUE.equals(accountDto.getEmailConfirmed());
        if (mailProperties.isMailConfigured() && !emailConfirmed) {
            throw new BusinessException("account.email-not-confirmed");
        }
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String accountId) {
        return accountRoleService.retrieveAccountAuthorities(accountId);
    }

}
