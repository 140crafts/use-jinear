package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceInvitationInitializeVo {

    private String email;
    private String workspaceId;
    private String initialTeamId;
    private LocaleType preferredLocale;
    private String invitedBy;
    private WorkspaceAccountRoleType forRole;
}
