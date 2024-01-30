package co.jinear.core.converter.calendar;

import co.jinear.core.converter.integration.IntegrationInfoDtoConverter;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.entity.calendar.Calendar;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.integration.IntegrationScope;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = IntegrationInfoDtoConverter.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CalendarDtoConverter {

    @Mapping(source = "calendar.integrationInfo.provider", target = "provider")
    CalendarDto convert(Calendar calendar);

    @AfterMapping
    default void afterMap(@MappingTarget CalendarDto calendarDto, Calendar calendar) {
        mapScopes(calendarDto, calendar);
    }

    default void mapScopes(CalendarDto calendarDto, Calendar calendar) {
        Optional.of(calendar)
                .map(Calendar::getIntegrationInfo)
                .map(IntegrationInfo::getScopes)
                .map(integrationScopes -> integrationScopes
                        .stream()
                        .map(IntegrationScope::getScope)
                        .toList())
                .ifPresent(calendarDto::setScopes);
    }
}
