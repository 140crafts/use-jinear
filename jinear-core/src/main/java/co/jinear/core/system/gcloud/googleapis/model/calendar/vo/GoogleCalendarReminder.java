package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarReminder {
    private Boolean useDefault;
    private List<GoogleCalendarReminderOverride> overrides;
}
