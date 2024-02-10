package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private GoogleCalendarExtendedProperties extendedProperties;
    private String hangoutLink;
    private GoogleCalendarConferenceData conferenceData;
    private GoogleCalendarGadget gadget;
    private Boolean anyoneCanAddSelf;
    private Boolean guestsCanInviteOthers;
    private Boolean guestsCanModify;
    private Boolean guestsCanSeeOtherGuests;
    private Boolean privateCopy;
    private Boolean locked;
    private GoogleCalendarReminder reminders;
    private GoogleCalendarSource source;
    private GoogleCalendarWorkingLocationProperties workingLocationProperties;
    private GoogleCalendarOutOfOfficeProperties outOfOfficeProperties;
    private GoogleCalendarFocusTimeProperties focusTimeProperties;
    private List<GoogleCalendarAttachment> attachments;
    private String eventType;
}
