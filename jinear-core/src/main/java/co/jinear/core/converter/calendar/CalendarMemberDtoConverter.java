package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.CalendarMemberDto;
import co.jinear.core.model.entity.calendar.CalendarMember;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {CalendarDtoConverter.class})
public interface CalendarMemberDtoConverter {

    @Mapping(source = "account.username.username", target = "account.username")
    CalendarMemberDto convert(CalendarMember calendarMember);
}
