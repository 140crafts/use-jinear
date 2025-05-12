package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class TaskProjectAndMilestoneUpdateRequest {
    @Nullable
    private String projectId;
    @Nullable
    private String milestoneId;
}
