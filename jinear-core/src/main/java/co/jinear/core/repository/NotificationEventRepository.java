package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent, String> {

    Page<NotificationEvent> findAllByWorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String accountId, Pageable pageable);

    Long countAllByWorkspaceIdAndAccountIdAndIsReadAndPassiveIdIsNull(String workspaceId, String accountId, Boolean isRead);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NotificationEvent notificationEvent
                set notificationEvent.isRead = true, notificationEvent.lastUpdatedDate = current_timestamp() 
                    where 
                        notificationEvent.accountId = :accountId and 
                        notificationEvent.workspaceId = :workspaceId and 
                        notificationEvent.createdDate < :before and 
                        notificationEvent.passiveId is null
                """)
    void updateAllBeforeAsRead(@Param("accountId") String accountId, @Param("workspaceId") String workspaceId, @Param("before") Date before);
}
