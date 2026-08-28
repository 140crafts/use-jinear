package co.jinear.core.service.account;

import co.jinear.core.config.properties.GenericJinearProperties;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.account.AccountRole;
import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.model.vo.account.password.AccountPasswordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementAccountOperationService {

    private final GenericJinearProperties genericJinearProperties;
    private final AccountRoleService accountRoleService;
    private final AccountRetrieveService accountRetrieveService;
    private final AccountInitializeService accountInitializeService;
    private final AccountPasswordService accountPasswordService;
    private final ManagementAccountLockService managementAccountLockService;

    @Transactional
    public void syncAdminAccount() {
        managementAccountLockService.lockForManagementAccountRoleSync();
        try {
            List<AccountRole> currentAdminRoles = accountRoleService.retrieveAllWithRole(RoleType.ADMIN);

            if (!Boolean.TRUE.equals(genericJinearProperties.getManagementEnabled())) {
                log.info("Management disabled. Revoking admin role from {} account(s).", currentAdminRoles.size());
                currentAdminRoles.forEach(accountRoleService::retainRole);
                return;
            }

            String adminEmail = genericJinearProperties.getAdminEmail();
            String adminPassword = genericJinearProperties.getAdminPassword();

            if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
                log.warn("Management enabled but admin email or password is not configured. Skipping admin sync.");
                return;
            }

            revokeAdminRoleFromOtherAccounts(currentAdminRoles, adminEmail);

            String adminAccountId = resolveAdminAccountId(currentAdminRoles, adminEmail, adminPassword);
            updatePassword(adminAccountId, adminPassword);

            log.info("Admin account is ready. accountId: {}, adminEmail: {}", adminAccountId, adminEmail);
        } finally {
            managementAccountLockService.unlockForManagementAccountRoleSync();
        }
    }

    private void revokeAdminRoleFromOtherAccounts(List<AccountRole> currentAdminRoles, String adminEmail) {
        currentAdminRoles.stream()
                .filter(accountRole -> !adminEmail.equalsIgnoreCase(accountRole.getAccount().getEmail()))
                .forEach(accountRole -> {
                    log.info("Revoking admin role, email does not match configured admin. accountId: {}",
                            accountRole.getAccount().getAccountId());
                    accountRoleService.retainRole(accountRole);
                });
    }

    private String resolveAdminAccountId(List<AccountRole> currentAdminRoles, String adminEmail, String adminPassword) {
        return findCurrentAdminAccountId(currentAdminRoles, adminEmail)
                .or(() -> promoteExistingAccount(adminEmail))
                .orElseGet(() -> createAdminAccount(adminEmail, adminPassword));
    }

    private Optional<String> findCurrentAdminAccountId(List<AccountRole> currentAdminRoles, String adminEmail) {
        return currentAdminRoles.stream()
                .map(AccountRole::getAccount)
                .filter(account -> adminEmail.equalsIgnoreCase(account.getEmail()))
                .map(Account::getAccountId)
                .findFirst();
    }

    private Optional<String> promoteExistingAccount(String adminEmail) {
        return accountRetrieveService.retrieveByEmailOptional(adminEmail)
                .map(AccountDto::getAccountId)
                .map(accountId -> {
                    log.info("Account exists without admin role. Assigning admin role. accountId: {}", accountId);
                    accountRoleService.assignRoleToAccount(accountId, RoleType.ADMIN);
                    return accountId;
                });
    }

    private String createAdminAccount(String adminEmail, String adminPassword) {
        log.info("No account found for configured admin email. Creating one.");
        AccountInitializeVo accountInitializeVo = new AccountInitializeVo();
        accountInitializeVo.setEmail(adminEmail);
        accountInitializeVo.setPassword(adminPassword);
        AccountDto accountDto = accountInitializeService.initializeAccount(accountInitializeVo);
        accountRoleService.assignRoleToAccount(accountDto.getAccountId(), RoleType.ADMIN);
        return accountDto.getAccountId();
    }

    private void updatePassword(String accountId, String adminPassword) {
        accountPasswordService.updateAccountPassword(AccountPasswordVo.builder()
                .accountId(accountId)
                .clearText(adminPassword)
                .autoGenerated(Boolean.FALSE)
                .build());
    }
}