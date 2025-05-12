package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeWorkspaceMemberVo {
    private String accountId;
    private String workspaceId;
    private WorkspaceAccountRoleType role;
}
