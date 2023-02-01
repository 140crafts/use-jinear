package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.vo.task.TaskRelationInitializeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskRelationConverter {

    TaskRelation map(TaskRelationInitializeVo taskRelationInitializeVo);

    @Mapping(source = "task.owner.username.username",target = "task.owner.username")
    @Mapping(source = "task.assignedToAccount.username.username",target = "task.assignedToAccount.username")
    @Mapping(source = "task.workspace.username.username",target = "task.workspace.username")
    @Mapping(source = "relatedTask.owner.username.username",target = "relatedTask.owner.username")
    @Mapping(source = "relatedTask.assignedToAccount.username.username",target = "relatedTask.assignedToAccount.username")
    @Mapping(source = "relatedTask.workspace.username.username",target = "relatedTask.workspace.username")
    TaskRelationDto map(TaskRelation taskRelation);
}
