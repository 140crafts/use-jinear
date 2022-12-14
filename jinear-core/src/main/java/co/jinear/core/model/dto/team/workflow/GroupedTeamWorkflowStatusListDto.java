package co.jinear.core.model.dto.team.workflow;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class GroupedTeamWorkflowStatusListDto {
    private HashMap<TeamWorkflowStateGroup, List<TeamWorkflowStatusDto>> groupedTeamWorkflowStatuses;
}
