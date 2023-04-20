package co.jinear.core.manager.notification;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notification.NotificationMessageDto;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.notification.RetrieveUnreadNotificationEventCountResponse;
import co.jinear.core.model.vo.notification.NotificationEventListingVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.notification.NotificationEventListingService;
import co.jinear.core.service.notification.NotificationEventOperationService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventListingManager {

    private final SessionInfoService sessionInfoService;
    private final NotificationEventListingService notificationEventListingService;
    private final WorkspaceValidator workspaceValidator;
    private final NotificationEventOperationService notificationEventOperationService;

    public NotificationEventListingResponse retrieveNotifications(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve notifications has started. accountId: {}, workspaceId: {}, page: {}", currentAccountId, workspaceId, page);
        NotificationEventListingVo notificationEventListingVo = mapVo(workspaceId, currentAccountId, page);
        Page<NotificationMessageDto> eventDtoPage = notificationEventListingService.retrieveNotificationEvents(notificationEventListingVo);
        notificationEventOperationService.updateAllAsRead(currentAccountId, workspaceId);
        return mapUnreadCountResponse(eventDtoPage);
    }

    public RetrieveUnreadNotificationEventCountResponse retrieveUnreadNotificationCount(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve unread notification count has started. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        Long count = notificationEventListingService.retrieveUnreadNotificationEventCount(workspaceId,currentAccountId);
            return mapUnreadCountResponse(count);
    }

    @NonNull
    private RetrieveUnreadNotificationEventCountResponse mapUnreadCountResponse(Long count) {
        RetrieveUnreadNotificationEventCountResponse retrieveUnreadNotificationEventCountResponse = new RetrieveUnreadNotificationEventCountResponse();
        retrieveUnreadNotificationEventCountResponse.setUnreadNotificationCount(count);
        return retrieveUnreadNotificationEventCountResponse;
    }

    @NonNull
    private NotificationEventListingResponse mapUnreadCountResponse(Page<NotificationMessageDto> eventDtoPage) {
        NotificationEventListingResponse notificationEventListingResponse = new NotificationEventListingResponse();
        notificationEventListingResponse.setEventDtoPage(new PageDto<>(eventDtoPage));
        return notificationEventListingResponse;
    }

    private NotificationEventListingVo mapVo(String workspaceId, String currentAccountId, int page) {
        NotificationEventListingVo notificationEventListingVo = new NotificationEventListingVo();
        notificationEventListingVo.setWorkspaceId(workspaceId);
        notificationEventListingVo.setCurrentAccountId(currentAccountId);
        notificationEventListingVo.setPage(page);
        return notificationEventListingVo;
    }
}
