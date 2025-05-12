package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRetrieveAllRequest extends BaseRequest {
    private String workspaceId;
    private String teamId;
    private int page;
}
