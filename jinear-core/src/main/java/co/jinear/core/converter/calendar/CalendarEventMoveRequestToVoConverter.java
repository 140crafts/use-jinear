package co.jinear.core.converter.calendar;

import co.jinear.core.model.request.calendar.CalendarEventMoveRequest;
import co.jinear.core.model.vo.calendar.MoveExternalEventVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalendarEventMoveRequestToVoConverter {

    MoveExternalEventVo convert(CalendarEventMoveRequest calendarEventMoveRequest);
}
