package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarWorkingLocationProperties {
    private String type;
    private Object homeOffice;
    private GoogleCalendarCustomLocation customLocation;
    private GoogleCalendarOfficeLocation officeLocation;
}
