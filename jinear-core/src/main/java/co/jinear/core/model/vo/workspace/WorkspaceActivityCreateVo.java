package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceActivityCreateVo {
    private String workspaceId;
    private String accountId;
    private String relatedObjectId;
    private WorkspaceActivityType type;
}
