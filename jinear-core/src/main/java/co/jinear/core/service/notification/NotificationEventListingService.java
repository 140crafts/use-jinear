package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationEventDtoConverter;
import co.jinear.core.model.dto.notification.NotificationEventDto;
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

    private static final int PAGE_SIZE = 50;

    private final NotificationEventRepository notificationEventRepository;
    private final NotificationEventDtoConverter notificationEventDtoConverter;

    public Page<NotificationEventDto> retrieveNotificationEvents(String accountId, int page) {
        log.info("Retrieve notification events has started. accountId: {}, page: {}", accountId, page);
        return notificationEventRepository.findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(accountId, PageRequest.of(page, PAGE_SIZE))
                .map(notificationEventDtoConverter::convert);
    }
}
