package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.calendar.CalendarEventSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskDtoToCalendarEventDtoMapper {

    public CalendarEventDto map(TaskDto taskDto) {
        CalendarEventDto calendarEventDto = new CalendarEventDto();
        calendarEventDto.setCalendarEventId(taskDto.getTaskId());
        calendarEventDto.setTitle(taskDto.getTitle());
        calendarEventDto.setAssignedDate(taskDto.getAssignedDate());
        calendarEventDto.setDueDate(taskDto.getDueDate());
        calendarEventDto.setHasPreciseAssignedDate(taskDto.getHasPreciseAssignedDate());
        calendarEventDto.setHasPreciseDueDate(taskDto.getHasPreciseDueDate());
        calendarEventDto.setCalendarEventSourceType(CalendarEventSourceType.TASK);
        calendarEventDto.setDescription(taskDto.getDescription());
        calendarEventDto.setRelatedTask(taskDto);

        return calendarEventDto;
    }
}
