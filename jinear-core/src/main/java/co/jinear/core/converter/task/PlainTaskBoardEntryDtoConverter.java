package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlainTaskBoardEntryDtoConverter {

    PlainTaskBoardEntryDto mapPlain(TaskBoardEntry taskBoardEntry);
}
