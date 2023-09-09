package co.jinear.core.service.notification.provider;

import co.jinear.core.model.dto.notification.NotificationMessageExternalDataDto;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseNotificationDeliveryStrategy implements NotificationDeliveryStrategy {

    private static final String NOTIFICATION_IMAGE = "https://jinear.co/icons/notification-icon.png";

    @Override
    public void send(NotificationMessageVo notificationMessageVo) {
        log.info("Firebase notification send has started. notificationMessageVo: {}", notificationMessageVo);
        Notification notification = initializeNotification(notificationMessageVo);
        Message message = initializeMessage(notificationMessageVo, notification);
        send(message);
        log.info("Firebase notification send has completed. notificationMessageVo: {}", notificationMessageVo);
    }

    private static void send(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Firebase response {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase send message has failed.");
        }
    }

    @Override
    public NotificationProviderType getProviderType() {
        return NotificationProviderType.FIREBASE;
    }

    private Message initializeMessage(NotificationMessageVo notificationMessageVo, Notification notification) {
        Map<String, String> map = new HashMap<>();

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getLaunchUrl)
                .ifPresent(param -> map.put("launchUrl", param));

        WebpushConfig webpushConfig = Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getLaunchUrl)
                .map(WebpushFcmOptions::withLink)
                .map(webpushFcmOptions -> WebpushConfig.builder().setFcmOptions(webpushFcmOptions).build())
                .orElse(null);


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

        return Message.builder()
                .putAllData(map)
                .setNotification(notification)
                .setWebpushConfig(webpushConfig)
                .setToken(notificationMessageVo.getTarget().getExternalTargetId())
                .build();
    }

    private Notification initializeNotification(NotificationMessageVo notificationMessageVo) {
        return Notification.builder()
                .setTitle(notificationMessageVo.getTitle())
                .setBody(notificationMessageVo.getText())
                .setImage(NOTIFICATION_IMAGE)
                .build();
    }
}
