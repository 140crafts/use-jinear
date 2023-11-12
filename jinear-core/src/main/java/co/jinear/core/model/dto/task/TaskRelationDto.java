package co.jinear.core.model.dto.task;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class TaskRelationDto {
    private String taskRelationId;
    @Nullable
    private String taskId;
    @Nullable
    private String relatedTaskId;
    private TaskRelationType relationType;
    @Nullable
    private RelatedTaskDto task;
    @Nullable
    private RelatedTaskDto relatedTask;
}
