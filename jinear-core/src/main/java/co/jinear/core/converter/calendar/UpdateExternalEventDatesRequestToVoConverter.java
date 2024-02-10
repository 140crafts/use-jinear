package co.jinear.core.converter.calendar;

import co.jinear.core.model.request.calendar.CalendarEventDateUpdateRequest;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateExternalEventDatesRequestToVoConverter {

    UpdateExternalEventDatesVo convert(CalendarEventDateUpdateRequest calendarEventDateUpdateRequest);
}
