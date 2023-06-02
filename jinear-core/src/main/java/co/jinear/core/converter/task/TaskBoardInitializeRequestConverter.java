package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskBoardInitializeRequest;
import co.jinear.core.model.vo.task.InitializeTaskBoardVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskBoardInitializeRequestConverter {

    InitializeTaskBoardVo convert(TaskBoardInitializeRequest taskBoardInitializeRequest, String ownerId);
}
