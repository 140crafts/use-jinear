package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCalendarInfo {
    private String kind;
    private String etag;
    private String id;
    private String summary;
    private String description;
    private String location;
    private String timeZone;
    private String summaryOverride;
    private String colorId;
    private String backgroundColor;
    private String foregroundColor;
    private String hidden;
    private String selected;
    private String accessRole;
    private List<GoogleCalendarReminderInfo> defaultReminders;
    private GoogleCalendarNotificationSettings googleCalendarNotificationSettings;
    @JsonProperty("private")
    private Boolean calendarPrivate;
    @JsonProperty("deleted")
    private Boolean calendarDeleted;
    private GoogleCalendarConferenceProperties conferenceProperties;
}
