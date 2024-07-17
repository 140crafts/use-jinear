package co.jinear.core.model.vo.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InitializeExternalEventVo {

    private String calendarId;
    private String calendarSourceId;
    private String summary;
    private String description;
    private String location;

    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Boolean hasPreciseAssignedDate;
    private Boolean hasPreciseDueDate;
}
