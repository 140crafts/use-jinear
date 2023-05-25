package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskListUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskListUpdateStateRequest;
import co.jinear.core.model.request.task.TaskListUpdateTitleRequest;
import co.jinear.core.model.vo.task.UpdateTaskListDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskListStateVo;
import co.jinear.core.model.vo.task.UpdateTaskListTitleVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskListUpdateRequestConverter {

    UpdateTaskListDueDateVo convert(TaskListUpdateDueDateRequest taskListUpdateDueDateRequest);

    UpdateTaskListTitleVo convert(TaskListUpdateTitleRequest taskListUpdateTitleRequest);

    UpdateTaskListStateVo convert(TaskListUpdateStateRequest taskListUpdateStateRequest);
}
