package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarEntryPoint {
    private String entryPointType;
    private String uri;
    private String label;
    private String pin;
    private String accessCode;
    private String meetingCode;
    private String passcode;
    private String password;
}
