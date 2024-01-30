package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCalendarEventInfo {

    private String kind;
    private String etag;
    private String id;
    private String status;
    private String htmlLink;
    private String created;
    private String updated;
    private String summary;
    private String description;
    private String location;
    private String colorId;
    private GoogleCalendarEventAttendee creator;
    private GoogleCalendarEventAttendee organizer;
    private GoogleCalendarEventDate start;
    private GoogleCalendarEventDate end;
    private Boolean endTimeUnspecified;
    private List<String> recurrence;
    private String recurringEventId;
    private GoogleCalendarEventDate originalStartTime;
    private String transparency;
    private String visibility;
    private String iCalUID;
    private Integer sequence;
    private List<GoogleCalendarEventAttendee> attendees;
    private Boolean attendeesOmitted;
    private String hangoutLink;
    private Boolean anyoneCanAddSelf;
    private Boolean guestsCanInviteOthers;
    private Boolean guestsCanModify;
    private Boolean guestsCanSeeOtherGuests;
    private Boolean privateCopy;
    private Boolean locked;
    private String eventType;
}
