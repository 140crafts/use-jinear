package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCalendarEventAttendee {
    private String id;
    private String email;
    private String displayName;
    private String organizer;
    private String self;
    private String resource;
    private String optional;
    private String responseStatus;
    private String comment;
    private Integer additionalGuests;
}
