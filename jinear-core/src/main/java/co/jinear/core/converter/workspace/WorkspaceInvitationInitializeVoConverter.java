package co.jinear.core.converter.workspace;

import co.jinear.core.model.request.workspace.WorkspaceMemberInviteRequest;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceInvitationInitializeVoConverter {

    @Mapping(target = "preferredLocale", source = "workspaceMemberInviteRequest.locale")
    WorkspaceInvitationInitializeVo convert(WorkspaceMemberInviteRequest workspaceMemberInviteRequest,String invitedBy);
}
