package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.vo.calendar.AddCalendarMemberVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddCalendarMemberVoConverter {

    AddCalendarMemberVo map(CalendarDto calendarDto, String accountId);
}
