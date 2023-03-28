package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.response.workspace.WorkspaceInvitationInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceInvitationInfoResponseConverter {

//        @Mapping(source = "workspaceInvitationInfoDto", target = "workspaceInvitationInfoDto")
        WorkspaceInvitationInfoResponse convert(WorkspaceInvitationInfoDto workspaceInvitationInfoDto);
}
