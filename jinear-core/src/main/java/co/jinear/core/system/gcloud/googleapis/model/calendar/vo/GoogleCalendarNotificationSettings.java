package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCalendarNotificationSettings {
    private List<GoogleCalendarNotificationMethodInfo> notifications;
}
