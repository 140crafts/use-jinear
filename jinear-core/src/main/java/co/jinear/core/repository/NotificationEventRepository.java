package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.enumtype.notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent, String> {

    Page<NotificationEvent> findAllByWorkspaceIdAndAccountIdAndNotificationTypeIsNotInAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String accountId, List<NotificationType> excludeTypes, Pageable pageable);

    Page<NotificationEvent> findAllByWorkspaceIdAndTeamIdAndAccountIdAndNotificationTypeIsNotInAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, String accountId, List<NotificationType> excludeTypes, Pageable pageable);

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
    void updateAllBeforeAsRead(@Param("accountId") String accountId,
                               @Param("workspaceId") String workspaceId,
                               @Param("before") Date before);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NotificationEvent notificationEvent
                set notificationEvent.isRead = true, notificationEvent.lastUpdatedDate = current_timestamp() 
                    where 
                        notificationEvent.accountId = :accountId and 
                        notificationEvent.workspaceId = :workspaceId and 
                        notificationEvent.teamId = :teamId and 
                        notificationEvent.createdDate < :before and 
                        notificationEvent.passiveId is null
                """)
    void updateAllFromTeamBeforeAsRead(@Param("accountId") String accountId,
                                       @Param("workspaceId") String workspaceId,
                                       @Param("teamId") String teamId,
                                       @Param("before") Date before);
}
