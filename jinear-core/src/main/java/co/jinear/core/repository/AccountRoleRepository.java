package co.jinear.core.repository;

import co.jinear.core.model.entity.account.AccountRole;
import co.jinear.core.model.enumtype.account.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    List<AccountRole> findAllByAccountIdAndPassiveIdIsNull(String accountId);

    Optional<AccountRole> findByAccountIdAndRoleAndPassiveIdIsNull(String accountId, RoleType role);
}
