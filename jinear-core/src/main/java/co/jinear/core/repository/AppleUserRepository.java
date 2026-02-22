package co.jinear.core.repository;

import co.jinear.core.model.entity.account.AppleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface AppleUserRepository extends JpaRepository<AppleUser, String> {

    Optional<AppleUser> findByExternalAppleIdAndPassiveIdIsNull(String externalAppleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update AppleUser appleUser
                set appleUser.passiveId = :passiveId, appleUser.lastUpdatedDate = :udate
                    where
                        appleUser.accountId = :accountId and
                        appleUser.passiveId is null
            """)
    void deleteAppleUser(@Param("accountId") String accountId, @Param("passiveId") String passiveId, Date udate);

}
