package co.jinear.core.converter.calendar;

import co.jinear.core.model.request.calendar.CalendarEventInitializeRequest;
import co.jinear.core.model.vo.calendar.InitializeExternalEventVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalendarEventInitializeRequestToVoConverter {

    InitializeExternalEventVo convert(CalendarEventInitializeRequest calendarEventInitializeRequest);
}
