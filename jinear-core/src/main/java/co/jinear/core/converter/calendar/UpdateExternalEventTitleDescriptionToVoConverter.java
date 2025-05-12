package co.jinear.core.converter.calendar;

import co.jinear.core.model.request.calendar.CalendarEventTitleDescriptionUpdateRequest;
import co.jinear.core.model.vo.calendar.UpdateExternalEventTitleDescriptionVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateExternalEventTitleDescriptionToVoConverter {

    UpdateExternalEventTitleDescriptionVo converter(CalendarEventTitleDescriptionUpdateRequest calendarEventTitleDescriptionUpdateRequest);
}
