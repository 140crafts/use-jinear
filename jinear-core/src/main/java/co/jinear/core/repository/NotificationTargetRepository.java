package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTargetRepository extends JpaRepository<NotificationTarget, String> {

    Optional<NotificationTarget> findBySessionInfoIdAndPassiveIdIsNull(String sessionInfoId);

    Long countAllBySessionInfoIdAndPassiveIdIsNull(String sessionInfoId);
}
