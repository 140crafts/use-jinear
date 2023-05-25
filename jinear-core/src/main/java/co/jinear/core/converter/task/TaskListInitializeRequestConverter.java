package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskListInitializeRequest;
import co.jinear.core.model.vo.task.InitializeTaskListVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskListInitializeRequestConverter {

    InitializeTaskListVo convert(TaskListInitializeRequest taskListInitializeRequest, String ownerId);
}
