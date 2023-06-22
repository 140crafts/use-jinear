package co.jinear.core.model.dto.workspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DetailedWorkspaceMemberDto extends WorkspaceMemberDto {

    private WorkspaceDto workspace;
}
