package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskRelationInitializeRequest;
import co.jinear.core.model.vo.task.TaskRelationInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskRelationInitializeVoConverter {

    @Mapping(source = "taskRelationInitializeRequest.taskId", target = "taskId")
    @Mapping(source = "taskRelationInitializeRequest.relatedTaskId", target = "relatedTaskId")
    TaskRelationInitializeVo map(TaskRelationInitializeRequest taskRelationInitializeRequest, String currentAccountId, TaskDto taskDto);

    @AfterMapping
    default void afterMap(@MappingTarget TaskRelationInitializeVo taskRelationInitializeVo, String currentAccountId, TaskDto taskDto) {
        taskRelationInitializeVo.setPerformedBy(currentAccountId);
        taskRelationInitializeVo.setWorkspaceId(taskDto.getWorkspaceId());
        taskRelationInitializeVo.setTeamId(taskDto.getTeamId());
    }
}
