package co.jinear.core.model.dto.task;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRelationDto {
    private String taskRelationId;
    private String taskId;
    private String relatedTaskId;
    private TaskRelationType relationType;
    private RelatedTaskDto task;
    private RelatedTaskDto relatedTask;
}
