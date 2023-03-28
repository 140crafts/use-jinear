package co.jinear.core.converter.workspace;

import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InitializeWorkspaceMemberVoConverter {

    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "invitation.forRole", target = "role")
    InitializeWorkspaceMemberVo convert(WorkspaceInvitation invitation, String accountId);
}
