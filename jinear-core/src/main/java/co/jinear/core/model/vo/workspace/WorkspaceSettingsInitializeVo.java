package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.enumtype.workspace.WorkspaceVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceSettingsInitializeVo {
    private String workspaceId;
    private WorkspaceVisibilityType visibility;
    private WorkspaceJoinType joinType;
}
