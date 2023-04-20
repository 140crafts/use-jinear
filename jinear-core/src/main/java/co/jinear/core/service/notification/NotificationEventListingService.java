package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationEventDtoConverter;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.dto.notification.NotificationMessageDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.vo.notification.NotificationEventListingVo;
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
    private final NotificationTemplatePopulateService notificationTemplatePopulateService;

    public Page<NotificationMessageDto> retrieveNotificationEvents(NotificationEventListingVo notificationEventListingVo) {
        log.info("Retrieve notification events has started. notificationEventListingVo: {}", notificationEventListingVo);
        return notificationEventRepository
                .findAllByWorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        notificationEventListingVo.getWorkspaceId(),
                        notificationEventListingVo.getCurrentAccountId(),
                        PageRequest.of(notificationEventListingVo.getPage(), PAGE_SIZE))
                .map(this::convert);
    }

    public Long retrieveUnreadNotificationEventCount(String workspaceId, String currentAccountId) {
        log.info("Retrieve notification events has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        return notificationEventRepository.countAllByWorkspaceIdAndAccountIdAndIsReadAndPassiveIdIsNull(workspaceId, currentAccountId, Boolean.FALSE);
    }

    private NotificationMessageDto convert(NotificationEvent notificationEvent) {
        NotificationEventDto notificationEventDto = notificationEventDtoConverter.convert(notificationEvent);
        return notificationTemplatePopulateService.populate(notificationEventDto);
    }
}
