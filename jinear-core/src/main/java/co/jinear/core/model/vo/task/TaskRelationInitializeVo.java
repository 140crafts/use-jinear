package co.jinear.core.model.vo.task;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskRelationInitializeVo {
    private String taskId;
    private String relatedTaskId;
    private TaskRelationType relation;
    private String workspaceId;
    private String teamId;
    private String performedBy;
}
