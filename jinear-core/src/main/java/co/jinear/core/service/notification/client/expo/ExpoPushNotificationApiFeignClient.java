package co.jinear.core.service.notification.client.expo;

import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ExpoPushNotificationApiFeignClient",
        url = "${expo.push-notification.url}"
)
@ConditionalOnProperty(value = "mock.expo-push-notification", havingValue = "false")
public interface ExpoPushNotificationApiFeignClient extends ExpoPushNotificationApiClient {

    @PostMapping("/v2/push/send")
    void sendPushNotification(@RequestHeader("Authorization") String bearerToken,
                              @RequestBody ExpoPushNotificationRequest expoPushNotificationRequest);
}
