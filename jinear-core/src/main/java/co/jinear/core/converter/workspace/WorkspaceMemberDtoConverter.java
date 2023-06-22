package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {WorkspaceDtoConverter.class})
public interface WorkspaceMemberDtoConverter {

    WorkspaceMemberDto map(WorkspaceMember workspaceMember);

    DetailedWorkspaceMemberDto mapToDetailed(WorkspaceMember workspaceMember);
}
