package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class WorkspaceActivityCreateVo {
    private String workspaceId;
    private String teamId;
    private String taskId;
    private String performedBy;
    private String relatedObjectId;
    private WorkspaceActivityType type;
    private String oldState;
    private String newState;
}
