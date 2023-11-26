package co.jinear.core.model.dto.team;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamTaskSourceType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class TeamDto extends BaseDto {
    private String teamId;
    private String workspaceId;
    private String workspaceUsername;
    private String name;
    private String username;
    private String tag;
    private TeamVisibilityType visibility;
    private TeamJoinMethodType joinMethod;
    private TeamTaskVisibilityType taskVisibility;
    private Set<TeamWorkflowStatusDto> workflowStatuses;
    private TeamTaskSourceType taskSourceType;
}
