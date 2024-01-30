package co.jinear.core.converter.google.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoogleCalendarInfoToExternalCalendarSourceDtoConverter {

    ExternalCalendarSourceDto convert(GoogleCalendarInfo googleCalendarInfo);
}
