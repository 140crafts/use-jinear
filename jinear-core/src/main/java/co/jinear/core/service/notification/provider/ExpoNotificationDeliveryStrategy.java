package co.jinear.core.service.notification.provider;

import co.jinear.core.model.dto.notification.NotificationMessageExternalDataDto;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.service.notification.client.expo.ExpoPushNotificationApiClient;
import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpoNotificationDeliveryStrategy implements NotificationDeliveryStrategy {

    private final ExpoPushNotificationApiClient expoPushNotificationApiClient;

    @Override
    public void send(NotificationMessageVo notificationMessageVo) {
        log.info("Expo notification send has started. notificationMessageVo: {}", notificationMessageVo);

        ExpoPushNotificationRequest expoPushNotificationRequest = mapMessage(notificationMessageVo);
        expoPushNotificationApiClient.sendPushNotification(expoPushNotificationRequest);

        log.info("Expo notification send has completed. notificationMessageVo: {}", notificationMessageVo);
    }

    @Override
    public NotificationProviderType getProviderType() {
        return NotificationProviderType.EXPO;
    }

    private ExpoPushNotificationRequest mapMessage(NotificationMessageVo notificationMessageVo) {
        ExpoPushNotificationRequest expoPushNotificationRequest = new ExpoPushNotificationRequest();
        Optional.of(notificationMessageVo).map(NotificationMessageVo::getTarget).map(NotificationTargetDto::getExternalTargetId).ifPresent(expoPushNotificationRequest::setTo);
        expoPushNotificationRequest.setTitle(notificationMessageVo.getTitle());
        expoPushNotificationRequest.setBody(notificationMessageVo.getText());
        expoPushNotificationRequest.setData(initializeMessage(notificationMessageVo));
        return expoPushNotificationRequest;
    }

    private Map<String, String> initializeMessage(NotificationMessageVo notificationMessageVo) {
        Map<String, String> map = new HashMap<>();

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getLaunchUrl)
                .ifPresent(param -> map.put("launchUrl", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getWorkspaceId)
                .ifPresent(param -> map.put("workspaceId", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getTeamId)
                .ifPresent(param -> map.put("teamId", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getTaskId)
                .ifPresent(param -> map.put("taskId", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getTaskTag)
                .ifPresent(param -> map.put("taskTag", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getNotificationType)
                .map(Enum::name)
                .ifPresent(param -> map.put("notificationType", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getData)
                .map(NotificationMessageExternalDataDto::getSenderSessionId)
                .ifPresent(param -> map.put("senderSessionInfoId", param));

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getTarget)
                .map(NotificationTargetDto::getSessionInfoId)
                .ifPresent(param -> map.put("targetSessionInfoId", param));

        return map;
    }
}
