package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.task.TaskListStateType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListUpdateStateRequest extends TaskListUpdateRequest {

    @NotNull
    private TaskListStateType state;
}
