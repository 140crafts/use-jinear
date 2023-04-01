package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInitializeRequest extends BaseRequest {
    @NotBlank
    private String workspaceId;
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String tag;
    private TeamVisibilityType visibility = TeamVisibilityType.VISIBLE;
    private TeamJoinMethodType joinMethod = TeamJoinMethodType.FROM_TEAM_ADMIN;
}
