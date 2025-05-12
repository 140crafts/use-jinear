package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationTarget;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationTargetRepository extends JpaRepository<NotificationTarget, String> {

    Optional<NotificationTarget> findFirstBySessionInfoIdAndPassiveIdIsNullOrderByCreatedDateDesc(String sessionInfoId);

    Optional<NotificationTarget> findFirstBySessionInfoIdAndExternalTargetIdAndPassiveIdIsNull(String sessionInfoId, String externalTargetId);

    List<NotificationTarget> findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String accountId, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NotificationTarget nt
                set nt.passiveId=:passiveId
                    where
                        nt.accountId = :accountId and
                        nt.passiveId is null
                """)
    void updateAllByAccountId(@Param("accountId") String accountId, @Param("passiveId") String passiveId);
}
