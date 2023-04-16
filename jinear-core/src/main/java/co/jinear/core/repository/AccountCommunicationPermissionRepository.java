package co.jinear.core.repository;

import co.jinear.core.model.entity.account.AccountCommunicationPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountCommunicationPermissionRepository extends JpaRepository<AccountCommunicationPermission, String> {

    Optional<AccountCommunicationPermission> findByAccountIdAndPassiveIdIsNull(String accountId);

}
