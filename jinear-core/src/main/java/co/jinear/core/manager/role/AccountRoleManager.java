package co.jinear.core.manager.role;

import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountRoleManager {

    private final SessionInfoService sessionInfoService;
    private final AccountRoleService accountRoleService;

    public void refreshCurrentAccountRoles() {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Refresh current account roles has started for account: {}", currentAccountId);
        Collection<GrantedAuthority> grantedAuthorities = accountRoleService.retrieveAccountAuthorities(currentAccountId);
        sessionInfoService.updateCurrentAccountRoles(grantedAuthorities.stream().toList());
        log.info("Refresh current account roles has finished for account: {}, new grantedAuthorities: {}", currentAccountId, StringUtils.join(grantedAuthorities, ", "));
    }
}
