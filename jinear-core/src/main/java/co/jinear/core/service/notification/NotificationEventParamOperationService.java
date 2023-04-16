package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationEventParamConverter;
import co.jinear.core.model.entity.notification.NotificationEventParam;
import co.jinear.core.repository.NotificationEventParamRepository;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventParamOperationService {

    private final NotificationEventParamRepository notificationEventParamRepository;
    private final NotificationEventParamConverter notificationEventParamConverter;

    @Transactional
    public void initializeEventParams(String notificationEventId, Map<String, String> params) {
        log.info("Initialize event params has started. notificationEventId: {}, params: {}", notificationEventId, StringUtils.join(params, NormalizeHelper.COMMA_SEPARATOR));
        params.forEach((paramKey, paramValue) -> initializeEventParam(notificationEventId, paramKey, paramValue));
    }

    private void initializeEventParam(String notificationEventId, String paramKey, String paramValue) {
        log.info("Initializing event param. notificationEventId: {}, paramKey: {}, paramValue: {}", notificationEventId, paramKey, paramValue);
        NotificationEventParam notificationEventParam = notificationEventParamConverter.map(notificationEventId, paramKey, paramValue);
        NotificationEventParam saved = notificationEventParamRepository.save(notificationEventParam);
        log.info("Event param initialized. notificationEventParamId: {}", saved.getNotificationEventParamId());
    }
}
