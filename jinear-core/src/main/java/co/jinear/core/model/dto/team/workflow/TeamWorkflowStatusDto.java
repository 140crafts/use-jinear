package co.jinear.core.model.dto.team.workflow;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamWorkflowStatusDto {

    private String teamWorkflowStatusId;
    private String teamId;
    private String workspaceId;
    private TeamWorkflowStateGroup workflowStateGroup;
    private String name;
    private Integer order;
    private Boolean editable;
    private Boolean removable;
}
