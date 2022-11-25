package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class WorkspaceMemberDto extends BaseDto {
    private String workspaceMemberId;
    private String workspaceId;
    private String accountId;
    private WorkspaceAccountRoleType role;
    private AccountDto accountDto;
}
