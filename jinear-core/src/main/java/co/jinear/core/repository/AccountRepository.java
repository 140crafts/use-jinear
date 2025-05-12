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

    @Query("from Account acc where acc.email = :email and acc.ghost= false and acc.passiveId is null")
    Optional<Account> findByEmailAndPassiveIdIsNull(@Param("email") String email);

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
    void updateLocaleType(@Param("accountId") String accountId, @Param("localeType") LocaleType localeType);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Account account
                set account.timeZone=:timeZone
                    where
                        account.accountId = :accountId and
                        account.passiveId is null
                """)
    void updateTimeZone(@Param("accountId") String accountId, @Param("timeZone") String timeZone);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Account account
                set account.ghost = true, account.email = ""
                    where
                        account.accountId = :accountId and
                        account.passiveId is null
                """)
    void updateGhostAndEmailAsEmpty(@Param("accountId") String accountId);
}