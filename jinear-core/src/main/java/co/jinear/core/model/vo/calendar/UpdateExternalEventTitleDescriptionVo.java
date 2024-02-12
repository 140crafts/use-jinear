package co.jinear.core.model.vo.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateExternalEventTitleDescriptionVo {

    private String calendarId;
    private String calendarSourceId;
    private String calendarEventId;
    private String title;
    private String description;
}
