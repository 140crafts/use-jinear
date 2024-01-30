package co.jinear.core.converter.google.calendar;


import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.enumtype.task.TaskSource;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarEventListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventDate;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.util.ZonedDateHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GoogleCalendarEventInfoToTaskDtoConverter {

    public TaskDto mapCalendarEventToTaskDto(String calendarId, GoogleCalendarEventListResponse googleCalendarEventListResponse, GoogleCalendarEventInfo googleCalendarEventInfo) {
        TaskDto taskDto = new TaskDto();
        mapGeneratedTaskId(googleCalendarEventInfo, taskDto);
        mapDescription(googleCalendarEventInfo, taskDto);
        mapDates(googleCalendarEventListResponse, googleCalendarEventInfo, taskDto);
        mapExternalCalendarSourceDto(calendarId, googleCalendarEventListResponse, taskDto);

        taskDto.setTitle(googleCalendarEventInfo.getSummary());
        taskDto.setTaskSource(TaskSource.GOOGLE_CALENDAR);
        taskDto.setGoogleCalendarEventInfo(googleCalendarEventInfo);
        taskDto.setTeamId(calendarId);

        return taskDto;
    }

    private void mapExternalCalendarSourceDto(String calendarId, GoogleCalendarEventListResponse googleCalendarEventListResponse, TaskDto taskDto) {
        ExternalCalendarSourceDto externalCalendarSourceDto = new ExternalCalendarSourceDto();
        externalCalendarSourceDto.setId(calendarId);
        externalCalendarSourceDto.setSummary(googleCalendarEventListResponse.getSummary());
        externalCalendarSourceDto.setDescription(googleCalendarEventListResponse.getDescription());
        externalCalendarSourceDto.setTimeZone(googleCalendarEventListResponse.getTimeZone());
        taskDto.setExternalCalendarSourceDto(externalCalendarSourceDto);
    }

    private void mapDescription(GoogleCalendarEventInfo googleCalendarEventInfo, TaskDto taskDto) {
        String eventDescription = googleCalendarEventInfo.getDescription();

        RichTextDto description = new RichTextDto();
        description.setRichTextId(taskDto.getTaskId() + "-description");
        description.setRelatedObjectId(taskDto.getTaskId());
        description.setType(RichTextType.TASK_DETAIL);
        description.setValue(eventDescription);

        taskDto.setDescription(description);
    }

    private void mapGeneratedTaskId(GoogleCalendarEventInfo googleCalendarEventInfo, TaskDto taskDto) {
        String generatedTaskId = googleCalendarEventInfo.getId() + googleCalendarEventInfo.getEtag();
        taskDto.setTaskId(generatedTaskId);
    }

    private void mapDates(GoogleCalendarEventListResponse googleCalendarEventListResponse, GoogleCalendarEventInfo googleCalendarEventInfo, TaskDto taskDto) {
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getStart)
                .map(GoogleCalendarEventDate::getDate)
                .map(dateStr -> ZonedDateHelper.parseWithDateTimeFormat4(dateStr, googleCalendarEventListResponse.getTimeZone()))
                .ifPresent(taskDto::setAssignedDate);
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getStart)
                .map(GoogleCalendarEventDate::getDateTime)
                .map(ZonedDateHelper::parseIsoDateTime)
                .ifPresent(startDateTime -> {
                    taskDto.setAssignedDate(startDateTime);
                    taskDto.setHasPreciseAssignedDate(Boolean.TRUE);
                });

        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getEnd)
                .map(GoogleCalendarEventDate::getDate)
                .map(dateStr -> ZonedDateHelper.parseWithDateTimeFormat4(dateStr, googleCalendarEventListResponse.getTimeZone()))
                .ifPresent(taskDto::setDueDate);
        Optional.of(googleCalendarEventInfo)
                .map(GoogleCalendarEventInfo::getEnd)
                .map(GoogleCalendarEventDate::getDateTime)
                .map(ZonedDateHelper::parseIsoDateTime)
                .ifPresent(startDateTime -> {
                    taskDto.setDueDate(startDateTime);
                    taskDto.setHasPreciseDueDate(Boolean.TRUE);
                });
    }
}
