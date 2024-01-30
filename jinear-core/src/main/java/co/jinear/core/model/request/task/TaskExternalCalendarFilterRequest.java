package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskExternalCalendarFilterRequest {
    private String integrationInfoId;
    private String calendarSourceId;
}
