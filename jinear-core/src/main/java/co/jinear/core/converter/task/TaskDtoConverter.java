package co.jinear.core.converter.task;

import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.converter.project.ProjectDtoConverter;
import co.jinear.core.converter.team.TeamDtoConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.task.UpdateTaskWorkflowDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.entity.task.TaskRelation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TeamDtoConverter.class, AccessibleMediaDtoConverter.class, ProjectDtoConverter.class})
public interface TaskDtoConverter {

    UpdateTaskWorkflowDto map(TaskDto taskDto, String remindersPassiveId);

    @Mapping(source = "owner.username.username", target = "owner.username")
    @Mapping(source = "assignedToAccount.username.username", target = "assignedToAccount.username")
    @Mapping(source = "workspace.username.username", target = "workspace.username")
    @Mapping(source = "team.workspace.username.username", target = "team.workspaceUsername")
    @Mapping(source = "owner.accountProfileMedia.media", target = "owner.profilePicture")
    @Mapping(source = "assignedToAccount.accountProfileMedia.media", target = "assignedToAccount.profilePicture")
    TaskDto map(Task task);

    @Mapping(source = "task.owner.username.username", target = "task.owner.username")
    @Mapping(source = "task.assignedToAccount.username.username", target = "task.assignedToAccount.username")
    @Mapping(source = "task.workspace.username.username", target = "task.workspace.username")
    @Mapping(source = "relatedTask.owner.username.username", target = "relatedTask.owner.username")
    @Mapping(source = "relatedTask.assignedToAccount.username.username", target = "relatedTask.assignedToAccount.username")
    @Mapping(source = "relatedTask.workspace.username.username", target = "relatedTask.workspace.username")
    TaskRelationDto map(TaskRelation taskRelation);
}
