package co.jinear.core.service.notification.client.expo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpoPushNotificationRequest {
    @ToString.Exclude
    private String to;
    private String title;
    private String body;
    private Map<String, String> data;
}
