package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskBoardEntryDetailedDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskDtoConverter.class, TaskBoardDtoConverter.class})
public interface TaskBoardEntryDtoConverter {

    TaskBoardEntryDto map(TaskBoardEntry taskBoardEntry);

    TaskBoardEntryDetailedDto mapDetailed(TaskBoardEntry taskBoardEntry);
}
