package co.jinear.core.service.account;

import co.jinear.core.converter.account.AccountRoleDtoConverter;
import co.jinear.core.model.dto.account.AccountRoleDto;
import co.jinear.core.model.entity.account.AccountRole;
import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.repository.AccountRoleRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRoleService {

    private final AccountRoleRepository accountRoleRepository;
    private final PassiveService passiveService;
    private final AccountRoleDtoConverter accountRoleDtoConverter;

    public AccountRoleDto assignRoleToAccount(String accountId, RoleType roleType) {
        Optional<AccountRole> existingRole = findByAccountIdAndRole(accountId, roleType);
        return existingRole
                .map(accountRoleDtoConverter::map)
                .orElseGet(() -> assignRole(accountId, roleType));
    }

    public void retainRoleFromAccount(String accountId, RoleType roleType) {
        log.info("Retaining roleType: {} from accountId: {}", roleType, accountId);
        Optional<AccountRole> accountRoleOptional = findByAccountIdAndRole(accountId, roleType);
        accountRoleOptional.ifPresent(this::retainRole);
    }

    public Collection<GrantedAuthority> retrieveAccountAuthorities(String accountId) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<AccountRole> accountRoles = accountRoleRepository.findAllByAccountIdAndPassiveIdIsNull(accountId);
        accountRoles.stream()
                .map(AccountRole::getRole)
                .map(RoleType::getGrantedAuthorities)
                .forEach(grantedAuthorities::addAll);
        return grantedAuthorities;
    }

    private AccountRoleDto assignRole(String accountId, RoleType roleType) {
        log.info("Assigning roleType: {} to accountId: {}", roleType, accountId);
        AccountRole accountRole = new AccountRole();
        accountRole.setRole(roleType);
        accountRole.setAccountId(accountId);
        AccountRole saved = accountRoleRepository.save(accountRole);
        log.info("Assigned roleType: {} to accountId: {} with accountRoleId: {}", roleType, accountId, saved.getAccountRoleId());
        return accountRoleDtoConverter.map(accountRole);
    }

    private void retainRole(AccountRole accountRole) {
        log.info("Retain role has started.");
        String passiveId = passiveService.createSystemActionPassive(accountRole.getAccountId());
        accountRole.setPassiveId(passiveId);
        AccountRole saved = accountRoleRepository.save(accountRole);
        log.info("Retain role has ended. accountRoleId: {}", saved.getAccountRoleId());
    }

    private Optional<AccountRole> findByAccountIdAndRole(String accountId, RoleType roleType) {
        return accountRoleRepository.findByAccountIdAndRoleAndPassiveIdIsNull(accountId, roleType);
    }
}
