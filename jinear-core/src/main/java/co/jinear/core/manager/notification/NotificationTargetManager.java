package co.jinear.core.manager.notification;

import co.jinear.core.converter.notification.NotificationTargetInitializeRequestConverter;
import co.jinear.core.model.request.notification.NotificationTargetInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.notification.NotificationTargetInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.notification.NotificationTargetOperationService;
import co.jinear.core.service.notification.NotificationTargetRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTargetManager {

    private final SessionInfoService sessionInfoService;
    private final NotificationTargetOperationService notificationTargetOperationService;
    private final NotificationTargetInitializeRequestConverter notificationTargetInitializeRequestConverter;
    private final NotificationTargetRetrieveService notificationTargetRetrieveService;

    public BaseResponse initializeNotificationTarget(NotificationTargetInitializeRequest notificationTargetInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentSessionId = sessionInfoService.currentAccountSessionId();
        log.info("Initialize notification target has started. currentAccountId: {}, currentSessionId: {}", currentAccountId, currentSessionId);
        if (!hasNotificationTargetExists(currentSessionId)) {
            log.info("Notification target does not exists. Initializing now.");
            NotificationTargetInitializeVo notificationTargetInitializeVo = notificationTargetInitializeRequestConverter.convert(notificationTargetInitializeRequest, currentAccountId, currentSessionId);
            notificationTargetOperationService.initializeNotificationTarget(notificationTargetInitializeVo);
        }
        return new BaseResponse();
    }

    private boolean hasNotificationTargetExists(String currentSessionId) {
        return notificationTargetRetrieveService.hasNotificationTargetWithSessionInfoId(currentSessionId);
    }
}
