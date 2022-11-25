package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceContentVisibilityType;
import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.enumtype.workspace.WorkspaceVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceSettingDto extends BaseDto {

    private WorkspaceVisibilityType visibility;
    private WorkspaceContentVisibilityType contentVisibility;
    private WorkspaceJoinType joinType;
}
