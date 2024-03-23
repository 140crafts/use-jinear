package co.jinear.core.service.notification.client.expo;

import co.jinear.core.config.properties.ExpoProperties;
import co.jinear.core.service.notification.client.expo.request.ExpoPushNotificationRequest;
import co.jinear.core.service.notification.client.expo.response.ExpoPushNotificationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.expo-push-notification", havingValue = "false")
public class ExpoPushNotificationApiRestClient implements ExpoPushNotificationApiClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String SEND = "/v2/push/send";

    private final RestTemplate expoRestTemplate;
    private final ExpoProperties expoProperties;

    @Override
    public void sendPushNotification(ExpoPushNotificationRequest expoPushNotificationRequest) {
        String token = expoProperties.getToken();
        HttpHeaders headers = retrieveHeaders(token);
        HttpEntity<ExpoPushNotificationRequest> requestEntity = new HttpEntity<>(expoPushNotificationRequest, headers);
        expoRestTemplate.exchange(SEND, HttpMethod.POST, requestEntity, ExpoPushNotificationResponse.class);
    }

    private HttpHeaders retrieveHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }
}
