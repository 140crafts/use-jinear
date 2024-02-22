package co.jinear.core.converter.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Description;
import biweekly.property.Summary;
import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.task.TaskTagConverter;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.calendar.CalendarEventSourceType;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarEventsToICalConverter {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final FeProperties feProperties;
    private final TaskTagConverter taskTagConverter;

    public String getICalendarString(String workspaceId, List<CalendarEventDto> taskCalendarEventDtos){
        ICalendar iCal = getICalendar(workspaceId, taskCalendarEventDtos);
        return Biweekly.write(iCal).go();
    }

    public ICalendar getICalendar(String workspaceId, List<CalendarEventDto> taskCalendarEventDtos) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        String calendarName = workspaceDto.getTitle();
        ICalendar iCal = initializeICalendar(calendarName);
        taskCalendarEventDtos.stream()
                .map(calendarEventDto -> mapCalendarEventToVEvent(workspaceDto, calendarEventDto))
                .forEach(iCal::addEvent);
        return iCal;
    }

    private VEvent mapCalendarEventToVEvent(WorkspaceDto workspaceDto, CalendarEventDto calendarEventDto) {
        VEvent event = new VEvent();
        mapSummary(calendarEventDto, event);
        mapEventUrl(workspaceDto, calendarEventDto, event);
        mapEventDates(calendarEventDto, event);
        mapDescription(calendarEventDto, event);
        return event;
    }

    private void mapSummary(CalendarEventDto calendarEventDto, VEvent event) {
        Summary summary = event.setSummary(calendarEventDto.getTitle());
        event.setSummary(summary);
    }

    private void mapEventUrl(WorkspaceDto workspaceDto, CalendarEventDto calendarEventDto, VEvent event) {
        if (CalendarEventSourceType.TASK.equals(calendarEventDto.getCalendarEventSourceType())) {
            TaskDto taskDto = calendarEventDto.getRelatedTask();
            String taskTag = taskTagConverter.retrieveTaskTag(taskDto);
            String taskUrl = feProperties.getTaskUrl().replaceAll(Pattern.quote("{workspaceName}"), workspaceDto.getUsername())
                    .replaceAll(Pattern.quote("{taskTag}"), taskTag);
            event.setUrl(taskUrl);
        }
    }

    private void mapDescription(CalendarEventDto calendarEventDto, VEvent event) {
        Optional.of(calendarEventDto)
                .map(CalendarEventDto::getDescription)
                .map(RichTextDto::getValue)
                .map(Description::new)
                .ifPresent(event::setDescription);
    }

    private void mapEventDates(CalendarEventDto calendarEventDto, VEvent event) {
        Boolean startDateHasTime = Optional.of(calendarEventDto)
                .map(CalendarEventDto::getHasPreciseAssignedDate)
                .orElse(Boolean.FALSE);

        Optional.of(calendarEventDto)
                .map(CalendarEventDto::getAssignedDate)
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .map(date -> new DateStart(date, startDateHasTime))
                .ifPresent(event::setDateStart);

        Boolean endDateHasTime = Optional.of(calendarEventDto)
                .map(CalendarEventDto::getHasPreciseDueDate)
                .orElse(Boolean.FALSE);

        Optional.of(calendarEventDto)
                .map(CalendarEventDto::getDueDate)
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .map(date -> new DateEnd(date, endDateHasTime))
                .ifPresent(event::setDateEnd);
    }

    private ICalendar initializeICalendar(String calendarName) {
        ICalendar iCal = new ICalendar();
        iCal.setName(calendarName);
        iCal.setUrl(feProperties.getWorkspaceCalendarUrl().replace("{workspaceUsername}", calendarName));
        return iCal;
    }
}
