package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMemberDtoConverter {

    WorkspaceMemberDto map(WorkspaceMember workspaceMember);
}
