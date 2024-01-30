package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.calendar.IntegrationExternalCalendarSourceListDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IntegrationExternalCalendarSourceListDtoMapper {

    IntegrationExternalCalendarSourceListDto map(IntegrationInfoDto integrationInfo, List<ExternalCalendarSourceDto> calendarSources);
}
