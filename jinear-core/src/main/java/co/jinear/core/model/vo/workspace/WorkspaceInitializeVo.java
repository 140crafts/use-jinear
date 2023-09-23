package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.enumtype.workspace.WorkspaceVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceInitializeVo {
    private String ownerId;
    private String title;
    private String description;
    private String handle;
    private WorkspaceVisibilityType visibility;
    private WorkspaceJoinType joinType;
    private Boolean appendRandomStrOnCollision;
    private LocaleType locale;
}
