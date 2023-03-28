package co.jinear.core.converter.workspace;

import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkspaceInvitationInitializeVoToEntityConverter {

    WorkspaceInvitation convert(WorkspaceInvitationInitializeVo vo);

    @AfterMapping
    default void setDefaults(@MappingTarget WorkspaceInvitation workspaceInvitation) {
        workspaceInvitation.setStatus(WorkspaceInvitationStatusType.WAITING_FOR_ANSWER);
    }
}
