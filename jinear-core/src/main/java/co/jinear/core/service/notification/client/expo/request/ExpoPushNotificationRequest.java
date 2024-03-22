package co.jinear.core.service.notification.client.expo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ExpoPushNotificationRequest {
    @ToString.Exclude
    private String to;
    private String title;
    private String body;
    private Map<String, String> data;
}
