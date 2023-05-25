package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.entity.task.TaskList;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskListEntryDtoConverter.class})
public interface TaskListDtoConverter {

    TaskListDto convert(TaskList taskList);
}
