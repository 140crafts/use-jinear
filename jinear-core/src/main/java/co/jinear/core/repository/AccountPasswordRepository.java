package co.jinear.core.repository;

import co.jinear.core.model.entity.account.AccountPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountPasswordRepository extends JpaRepository<AccountPassword, String> {

    Optional<AccountPassword> findByAccountIdAndPassiveIdIsNull(String accountId);
}
