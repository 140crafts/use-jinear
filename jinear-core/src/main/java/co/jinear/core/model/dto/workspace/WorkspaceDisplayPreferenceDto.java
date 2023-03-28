package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class WorkspaceDisplayPreferenceDto {

    private String accountId;
    @Nullable
    private String preferredWorkspaceId;
    @Nullable
    private String preferredTeamId;
    @Nullable
    private WorkspaceDto workspace;
    @Nullable
    private TeamDto team;
    @Nullable
    private WorkspaceAccountRoleType workspaceRole;
}
