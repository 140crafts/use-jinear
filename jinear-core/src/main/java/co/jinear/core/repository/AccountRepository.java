package co.jinear.core.repository;

import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByAccountIdAndPassiveIdIsNull(String accountId);

    Optional<Account> findByEmailAndPassiveIdIsNull(String email);

    Long countAllByAccountIdAndPassiveIdIsNull(String accountId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Account account
                set account.emailConfirmed=:emailConfirmed
                    where 
                        account.accountId = :accountId and 
                        account.passiveId is null
                """)
    void updateEmailConfirmed(@Param("accountId") String accountId, @Param("emailConfirmed") boolean emailConfirmed);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Account account
                set account.localeType=:localeType
                    where 
                        account.accountId = :accountId and 
                        account.passiveId is null
                """)
    void updateLocaleType(@Param("accountId") String accountId, @Param("localeType")LocaleType localeType);
}