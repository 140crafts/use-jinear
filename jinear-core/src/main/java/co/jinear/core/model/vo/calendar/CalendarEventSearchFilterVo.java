package co.jinear.core.model.vo.calendar;

import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CalendarEventSearchFilterVo extends TaskSearchFilterVo {

    private List<String> calendarIdList;
}
