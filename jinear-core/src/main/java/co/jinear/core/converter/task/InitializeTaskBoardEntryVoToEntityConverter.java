package co.jinear.core.converter.task;

import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeTaskBoardEntryVoToEntityConverter {

    TaskBoardEntry map(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo, Integer order);
}
