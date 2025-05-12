package co.jinear.core.converter.workspace;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskDtoConverter.class})
public interface WorkspaceActivityConverter {

    @Mapping(source = "performedByAccount.username.username", target = "performedByAccount.username")
    @Mapping(source = "relatedAccount.username.username", target = "relatedAccount.username")
    WorkspaceActivityDto map(WorkspaceActivity workspaceActivity);
}
