package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationEventDtoConverter;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.vo.notification.NotificationEventListingVo;
import co.jinear.core.model.vo.notification.TeamNotificationEventListingVo;
import co.jinear.core.repository.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventListingService {

    private static final int PAGE_SIZE = 150;

    private final NotificationEventRepository notificationEventRepository;
    private final NotificationEventDtoConverter notificationEventDtoConverter;

    //TODO: cgds-461 exclude conversation notifications
    public Page<NotificationEventDto> retrieveNotificationEvents(NotificationEventListingVo notificationEventListingVo) {
        log.info("Retrieve notification events has started. notificationEventListingVo: {}", notificationEventListingVo);
        return notificationEventRepository
                .findAllByWorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        notificationEventListingVo.getWorkspaceId(),
                        notificationEventListingVo.getCurrentAccountId(),
                        PageRequest.of(notificationEventListingVo.getPage(), PAGE_SIZE))
                .map(notificationEventDtoConverter::convert);
    }

    public Page<NotificationEventDto> retrieveTeamNotificationEvents(TeamNotificationEventListingVo teamNotificationEventListingVo) {
        log.info("Retrieve team notification events has started. teamNotificationEventListingVo: {}", teamNotificationEventListingVo);
        return notificationEventRepository
                .findAllByWorkspaceIdAndTeamIdAndAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        teamNotificationEventListingVo.getWorkspaceId(),
                        teamNotificationEventListingVo.getTeamId(),
                        teamNotificationEventListingVo.getCurrentAccountId(),
                        PageRequest.of(teamNotificationEventListingVo.getPage(), PAGE_SIZE))
                .map(notificationEventDtoConverter::convert);
    }

    public Long retrieveUnreadNotificationEventCount(String workspaceId, String currentAccountId) {
        log.info("Retrieve notification events has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        return notificationEventRepository.countAllByWorkspaceIdAndAccountIdAndIsReadAndPassiveIdIsNull(workspaceId, currentAccountId, Boolean.FALSE);
    }
}
