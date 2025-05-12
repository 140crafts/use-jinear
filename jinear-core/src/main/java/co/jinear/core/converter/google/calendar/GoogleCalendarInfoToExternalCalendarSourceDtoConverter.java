package co.jinear.core.converter.google.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.system.gcloud.googleapis.model.calendar.enumtype.GoogleCalendarAccessRoleType;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface GoogleCalendarInfoToExternalCalendarSourceDtoConverter {

    @Mapping(source = "id", target = "externalCalendarSourceId")
    ExternalCalendarSourceDto convert(GoogleCalendarInfo googleCalendarInfo);

    @AfterMapping
    default void afterMap(@MappingTarget ExternalCalendarSourceDto externalCalendarSourceDto, GoogleCalendarInfo googleCalendarInfo) {
        Optional.of(googleCalendarInfo).map(GoogleCalendarInfo::getAccessRole).map(GoogleCalendarAccessRoleType::isReadOnly).ifPresent(externalCalendarSourceDto::setReadOnly);
    }
}
