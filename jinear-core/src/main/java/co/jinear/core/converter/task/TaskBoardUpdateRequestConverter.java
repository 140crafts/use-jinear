package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskBoardUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateStateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateTitleRequest;
import co.jinear.core.model.vo.task.UpdateTaskBoardDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardStateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardTitleVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskBoardUpdateRequestConverter {

    UpdateTaskBoardDueDateVo convert(TaskBoardUpdateDueDateRequest taskListUpdateDueDateRequest);

    UpdateTaskBoardTitleVo convert(TaskBoardUpdateTitleRequest taskListUpdateTitleRequest);

    UpdateTaskBoardStateVo convert(TaskBoardUpdateStateRequest taskListUpdateStateRequest);
}
