package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InitializeTeamWorkflowStatusRequest extends BaseRequest {
    @NotNull
    private TeamWorkflowStateGroup workflowStateGroup;
    @NotBlank
    private String name;
}
