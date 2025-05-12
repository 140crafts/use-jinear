package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarConferenceData {
    private GoogleCalendarConferenceDataRequest createRequest;
    private List<GoogleCalendarEntryPoint> entryPoints;
    private GoogleCalendarConferenceSolution conferenceSolution;
    private String conferenceId;
    private String signature;
    private String notes;
}
