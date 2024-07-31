package co.jinear.core.model.vo.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MoveExternalEventVo {

    private String calendarSourceId;
    private String targetCalendarSourceId;
    private String eventId;
}
