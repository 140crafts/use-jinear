package co.jinear.core.system.gcloud.googleapis.model.calendar.response;

import co.jinear.core.system.gcloud.googleapis.model.calendar.enumtype.GoogleCalendarAccessRoleType;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarReminderInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCalendarEventListResponse {
    private String kind;
    private String etag;
    private String summary;
    private String description;
    private String updated;
    private String timeZone;
    private GoogleCalendarAccessRoleType accessRole;
    private List<GoogleCalendarReminderInfo> defaultReminders;
    private String nextSyncToken;
    private String nextPageToken;
    private List<GoogleCalendarEventInfo> items;
}
