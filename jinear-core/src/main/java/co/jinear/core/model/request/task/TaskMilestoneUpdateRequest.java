package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class TaskMilestoneUpdateRequest {
    @Nullable
    private String milestoneId;
}
