package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializeTeamWorkflowStatusRequest extends BaseRequest {
    @NotNull
    private TeamWorkflowStateGroup workflowStateGroup;
    @NotBlank
    private String name;
}
