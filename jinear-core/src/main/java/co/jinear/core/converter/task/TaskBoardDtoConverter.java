package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.entity.task.TaskBoard;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskBoardEntryDtoConverter.class, TaskBoardEntryDtoConverter.class})
public interface TaskBoardDtoConverter {

    TaskBoardDto convert(TaskBoard taskBoard);
}
