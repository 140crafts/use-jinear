package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.model.entity.task.TaskListEntry;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskDtoConverter.class})
public interface TaskListEntryDtoConverter {

    TaskListEntryDto map(TaskListEntry taskListEntry);
}
