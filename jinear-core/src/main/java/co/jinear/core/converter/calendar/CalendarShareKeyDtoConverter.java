package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.CalendarShareKeyDto;
import co.jinear.core.model.entity.calendar.CalendarShareKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalendarShareKeyDtoConverter {

    CalendarShareKeyDto convert(CalendarShareKey calendarShareKey);
}
