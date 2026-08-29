package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamWorkflowStatusReorderRequest extends BaseRequest {
    @NotNull
    private TeamWorkflowStateGroup workflowStateGroup;
    @NotEmpty
    private List<String> orderedTeamWorkflowStatusIds;
}
