package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskBoardEntryInitializeRequestConverter {

    InitializeTaskBoardEntryVo convert(TaskBoardEntryInitializeRequest taskBoardEntryInitializeRequest);
}
