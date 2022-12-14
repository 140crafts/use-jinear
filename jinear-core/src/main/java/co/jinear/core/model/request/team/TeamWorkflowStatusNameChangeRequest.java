package co.jinear.core.model.request.team;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamWorkflowStatusNameChangeRequest extends BaseRequest {
    private String name;
}
