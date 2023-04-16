package co.jinear.core.manager.notification;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.notification.NotificationEventListingService;
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

    public NotificationEventListingResponse retrieveNotifications(int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve notifications has started. accountId: {}, page: {}", currentAccountId, page);
        Page<NotificationEventDto> eventDtoPage = notificationEventListingService.retrieveNotificationEvents(currentAccountId, page);
        return mapResponse(eventDtoPage);
    }

    @NonNull
    private NotificationEventListingResponse mapResponse(Page<NotificationEventDto> eventDtoPage) {
        NotificationEventListingResponse notificationEventListingResponse = new NotificationEventListingResponse();
        notificationEventListingResponse.setEventDtoPage(new PageDto<>(eventDtoPage));
        return notificationEventListingResponse;
    }
}
