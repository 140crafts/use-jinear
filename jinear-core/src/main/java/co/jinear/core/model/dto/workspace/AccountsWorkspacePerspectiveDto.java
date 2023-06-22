package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AccountsWorkspacePerspectiveDto extends WorkspaceDto {
    private WorkspaceAccountRoleType role;
}
