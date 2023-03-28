package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceInvitationDto extends BaseDto {

    private String workspaceInvitationId;
    private String workspaceId;
    private String invitedBy;
    private String accountId;
    private String email;
    private WorkspaceInvitationStatusType status;

}
