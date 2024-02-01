package co.jinear.core.converter.google.calendar;


import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.enumtype.calendar.CalendarEventSourceType;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarEventListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventDate;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.util.ZonedDateHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GoogleCalendarEventInfoToCalendarEventDtoConverter {

    public CalendarEventDto mapCalendarEventToCalendarEventDto(String calendarId, GoogleCalendarEventListResponse googleCalendarEventListResponse, GoogleCalendarEventInfo googleCalendarEventInfo) {
        CalendarEventDto calendarEventDto = new CalendarEventDto();
        calendarEventDto.setCalendarEventId(googleCalendarEventInfo.getId());
        calendarEventDto.setTitle(googleCalendarEventInfo.getSummary());
        calendarEventDto.setCalendarEventSourceType(CalendarEventSourceType.GOOGLE_CALENDAR);
        calendarEventDto.setRelatedGoogleCalendarEventInfo(googleCalendarEventInfo);

        mapDescription(googleCalendarEventInfo, calendarEventDto);
        mapDates(googleCalendarEventListResponse, googleCalendarEventInfo, calendarEventDto);
        mapExternalCalendarSourceDto(calendarId, googleCalendarEventListResponse, calendarEventDto);

        return calendarEventDto;
    }

    private void mapExternalCalendarSourceDto(String calendarId, GoogleCalendarEventListResponse googleCalendarEventListResponse, CalendarEventDto calendarEventDto) {
        ExternalCalendarSourceDto externalCalendarSourceDto = new ExternalCalendarSourceDto();
        externalCalendarSourceDto.setId(calendarId);
        externalCalendarSourceDto.setSummary(googleCalendarEventListResponse.getSummary());
        externalCalendarSourceDto.setDescription(googleCalendarEventListResponse.getDescription());
        externalCalendarSourceDto.setTimeZone(googleCalendarEventListResponse.getTimeZone());
        calendarEventDto.setExternalCalendarSourceDto(externalCalendarSourceDto);
    }

    private void mapDescription(GoogleCalendarEventInfo googleCalendarEventInfo, CalendarEventDto calendarEventDto) {
        String eventDescription = googleCalendarEventInfo.getDescription();

        RichTextDto description = new RichTextDto();
        description.setRichTextId(calendarEventDto.getCalendarEventId() + "-description");
        description.setRelatedObjectId(calendarEventDto.getCalendarEventId());
        description.setType(RichTextType.TASK_DETAIL);
        description.setValue(eventDescription);

        calendarEventDto.setDescription(description);
    }

    private void mapDates(GoogleCalendarEventListResponse googleCalendarEventListResponse, GoogleCalendarEventInfo googleCalendarEventInfo, CalendarEventDto calendarEventDto) {
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getStart)
                .map(GoogleCalendarEventDate::getDate)
                .map(dateStr -> ZonedDateHelper.parseWithDateTimeFormat4(dateStr, googleCalendarEventListResponse.getTimeZone()))
                .ifPresent(calendarEventDto::setAssignedDate);
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getStart)
                .map(GoogleCalendarEventDate::getDateTime)
                .map(ZonedDateHelper::parseIsoDateTime)
                .ifPresent(startDateTime -> {
                    calendarEventDto.setAssignedDate(startDateTime);
                    calendarEventDto.setHasPreciseAssignedDate(Boolean.TRUE);
                });

        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getEnd)
                .map(GoogleCalendarEventDate::getDate)
                .map(dateStr -> ZonedDateHelper.parseWithDateTimeFormat4(dateStr, googleCalendarEventListResponse.getTimeZone()))
                .ifPresent(calendarEventDto::setDueDate);
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getEnd)
                .map(GoogleCalendarEventDate::getDateTime)
                .map(ZonedDateHelper::parseIsoDateTime)
                .ifPresent(startDateTime -> {
                    calendarEventDto.setDueDate(startDateTime);
                    calendarEventDto.setHasPreciseDueDate(Boolean.TRUE);
                });
    }
}
