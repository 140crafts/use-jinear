package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class TaskAssigneeUpdateRequest {
    @Nullable
    private String assigneeId;
}
