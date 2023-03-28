package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceInvitationDtoConverter {

    WorkspaceInvitationDto convert(WorkspaceInvitation workspaceInvitation);
}
