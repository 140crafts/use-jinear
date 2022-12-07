package co.jinear.core.model.vo.team.workflow;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitializeTeamWorkflowStatusVo {
    private String teamId;
    private String workspaceId;
    private TeamWorkflowStateGroup workflowStateGroup;
    private String name;
}
