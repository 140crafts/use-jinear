package co.jinear.core.model.request.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.enumtype.workspace.WorkspaceVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class WorkspaceInitializeRequest extends BaseRequest {
    @NotBlank
    private String title;
    private String description;
    @Size(min = 3)
    @Size(max = 255)
    private String handle;
    private WorkspaceVisibilityType visibility = WorkspaceVisibilityType.HIDDEN_UNLISTED;
    private WorkspaceJoinType joinType = WorkspaceJoinType.WITH_REQUEST;
}
