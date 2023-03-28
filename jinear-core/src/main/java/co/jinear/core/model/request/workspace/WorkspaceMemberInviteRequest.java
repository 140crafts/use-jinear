package co.jinear.core.model.request.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberInviteRequest extends BaseRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String workspaceId;

    @NotBlank
    private String initialTeamId;

    @NotNull
    private WorkspaceAccountRoleType forRole;
}

