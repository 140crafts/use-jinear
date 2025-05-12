package co.jinear.core.service.notification.client.expo;

import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@ConditionalOnProperty(value = "mock.expo-push-notification", havingValue = "true", matchIfMissing = true)
public class ExpoPushNotificationApiMockClient implements ExpoPushNotificationApiClient {

    @Override
    public void sendPushNotification(ExpoPushNotificationRequest expoPushNotificationRequest) {
        log.info("[MOCK] Send push notification has started. expoPushNotificationRequest: {}", expoPushNotificationRequest);
    }
}
