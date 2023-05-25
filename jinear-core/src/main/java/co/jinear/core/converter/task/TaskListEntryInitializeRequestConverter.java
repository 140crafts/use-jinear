package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskListEntryInitializeRequest;
import co.jinear.core.model.vo.task.InitializeTaskListEntryVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskListEntryInitializeRequestConverter {

    InitializeTaskListEntryVo convert(TaskListEntryInitializeRequest taskListEntryInitializeRequest);
}
