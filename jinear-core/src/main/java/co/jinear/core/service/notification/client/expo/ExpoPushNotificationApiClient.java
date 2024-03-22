package co.jinear.core.service.notification.client.expo;

import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;

public interface ExpoPushNotificationApiClient {

    void sendPushNotification(String bearerToken, ExpoPushNotificationRequest expoPushNotificationRequest);
}
