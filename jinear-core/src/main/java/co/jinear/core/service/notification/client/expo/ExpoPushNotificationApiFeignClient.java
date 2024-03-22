package co.jinear.core.service.notification.client.expo;

import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;
import feign.Headers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ExpoPushNotificationApiFeignClient",
        url = "${expo.push-notification.url}"
)
@ConditionalOnProperty(value = "mock.expo-push-notification", havingValue = "false")
@Headers({"host: exp.host", "accept: application/json", "accept-encoding: gzip, deflate", "content-type: application/json"})
public interface ExpoPushNotificationApiFeignClient extends ExpoPushNotificationApiClient {

    @PostMapping("/v2/push/send")
    void sendPushNotification(@RequestBody ExpoPushNotificationRequest expoPushNotificationRequest);
}
