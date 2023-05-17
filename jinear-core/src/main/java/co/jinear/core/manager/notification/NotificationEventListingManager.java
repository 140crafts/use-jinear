package co.jinear.core.manager.notification;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.notification.RetrieveUnreadNotificationEventCountResponse;
import co.jinear.core.model.vo.notification.NotificationEventListingVo;
import co.jinear.core.model.vo.notification.TeamNotificationEventListingVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.notification.NotificationEventListingService;
import co.jinear.core.service.notification.NotificationEventOperationService;
import co.jinear.core.validator.team.TeamAccessValidator;
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
    private final TeamAccessValidator teamAccessValidator;

    public NotificationEventListingResponse retrieveNotifications(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve notifications has started. accountId: {}, workspaceId: {}, page: {}", currentAccountId, workspaceId, page);
        NotificationEventListingVo notificationEventListingVo = mapVo(workspaceId, currentAccountId, page);
        Page<NotificationEventDto> eventDtoPage = notificationEventListingService.retrieveNotificationEvents(notificationEventListingVo);
        notificationEventOperationService.updateAllAsRead(currentAccountId, workspaceId);
        return mapUnreadCountResponse(eventDtoPage);
    }

    public NotificationEventListingResponse retrieveTeamNotifications(String workspaceId, String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve notifications has started. accountId: {}, workspaceId: {}, teamId: {}, page: {}", currentAccountId, workspaceId, teamId, page);
        TeamNotificationEventListingVo teamNotificationEventListingVo = mapVo(workspaceId, teamId, currentAccountId, page);
        Page<NotificationEventDto> eventDtoPage = notificationEventListingService.retrieveTeamNotificationEvents(teamNotificationEventListingVo);
        notificationEventOperationService.updateAllAsRead(currentAccountId, teamId, workspaceId);
        return mapUnreadCountResponse(eventDtoPage);
    }

    public RetrieveUnreadNotificationEventCountResponse retrieveUnreadNotificationCount(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve unread notification count has started. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        Long count = notificationEventListingService.retrieveUnreadNotificationEventCount(workspaceId, currentAccountId);
        return mapUnreadCountResponse(count);
    }

    @NonNull
    private RetrieveUnreadNotificationEventCountResponse mapUnreadCountResponse(Long count) {
        RetrieveUnreadNotificationEventCountResponse retrieveUnreadNotificationEventCountResponse = new RetrieveUnreadNotificationEventCountResponse();
        retrieveUnreadNotificationEventCountResponse.setUnreadNotificationCount(count);
        return retrieveUnreadNotificationEventCountResponse;
    }

    @NonNull
    private NotificationEventListingResponse mapUnreadCountResponse(Page<NotificationEventDto> eventDtoPage) {
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

    private TeamNotificationEventListingVo mapVo(String workspaceId, String teamId, String currentAccountId, int page) {
        TeamNotificationEventListingVo teamNotificationEventListingVo = new TeamNotificationEventListingVo();
        teamNotificationEventListingVo.setWorkspaceId(workspaceId);
        teamNotificationEventListingVo.setTeamId(teamId);
        teamNotificationEventListingVo.setCurrentAccountId(currentAccountId);
        teamNotificationEventListingVo.setPage(page);
        return teamNotificationEventListingVo;
    }
}
