package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskDtoConverter {

    @Mapping(source = "owner.username.username",target = "owner.username")
    @Mapping(source = "assignedToAccount.username.username",target = "assignedToAccount.username")
    @Mapping(source = "workspace.username.username",target = "workspace.username")
    @Mapping(source = "team.workspace.username.username",target = "team.workspaceUsername")
    TaskDto map(Task task);
}
