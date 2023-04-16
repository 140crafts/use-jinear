package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent, String> {

    Page<NotificationEvent> findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String accountId, Pageable pageable);
}
