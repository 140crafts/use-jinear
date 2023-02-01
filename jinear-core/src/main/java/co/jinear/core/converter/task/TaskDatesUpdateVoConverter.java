package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskAssigneeUpdateRequest;
import co.jinear.core.model.request.task.TaskDateUpdateRequest;
import co.jinear.core.model.vo.task.TaskAssigneeUpdateVo;
import co.jinear.core.model.vo.task.TaskDatesUpdateVo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskDatesUpdateVoConverter {

    @BeanMapping(qualifiedByName = "withDateUpdateRequest")
    TaskDatesUpdateVo map(TaskDateUpdateRequest taskDateUpdateRequest, String taskId);

    @BeanMapping(qualifiedByName = "withAssigneeUpdateRequest")
    TaskAssigneeUpdateVo map(TaskAssigneeUpdateRequest taskAssigneeUpdateRequest, String taskId);

    @AfterMapping
    @Named("withDateUpdateRequest")
    default void afterMap(@MappingTarget TaskDatesUpdateVo taskDatesUpdateVo, String taskId) {
        taskDatesUpdateVo.setTaskId(taskId);
    }

    @AfterMapping
    @Named("withAssigneeUpdateRequest")
    default void afterMap(@MappingTarget TaskAssigneeUpdateVo taskAssigneeUpdateVo, String taskId) {
        taskAssigneeUpdateVo.setTaskId(taskId);
    }
}
