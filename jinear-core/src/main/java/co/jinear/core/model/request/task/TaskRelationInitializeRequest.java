package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaskRelationInitializeRequest extends BaseRequest {
    @NotBlank
    private String taskId;
    @NotBlank
    private String relatedTaskId;
    @NotNull
    private TaskRelationType relation;
}
