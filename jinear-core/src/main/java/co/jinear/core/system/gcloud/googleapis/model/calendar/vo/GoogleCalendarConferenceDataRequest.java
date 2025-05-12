package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCalendarConferenceDataRequest {
    private String requestId;
    private GoogleCalendarConferenceSolutionKey conferenceSolutionKey;
    private GoogleCalendarConferenceDataStatus status;
}
