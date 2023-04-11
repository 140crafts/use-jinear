package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationTarget;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationTargetRepository extends JpaRepository<NotificationTarget, String> {

    Optional<NotificationTarget> findBySessionInfoIdAndPassiveIdIsNull(String sessionInfoId);

    Long countAllBySessionInfoIdAndPassiveIdIsNull(String sessionInfoId);

    List<NotificationTarget> findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String accountId, Pageable pageable);
}
