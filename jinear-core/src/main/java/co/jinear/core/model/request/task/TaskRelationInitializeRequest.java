package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
