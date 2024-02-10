package co.jinear.core.converter.google.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoogleCalendarInfoToExternalCalendarSourceDtoConverter {

    @Mapping(source = "id", target = "externalCalendarSourceId")
    ExternalCalendarSourceDto convert(GoogleCalendarInfo googleCalendarInfo);
}
