package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAddTaskRelationInput {

    @NotBlank
    @McpField(value = "Task id of the left side of the relation, from search_tasks, list_tasks or get_task.", hint = "Call search_tasks to find it.")
    private String taskId;

    @NotBlank
    @McpField(value = "Task id of the right side of the relation. It must be in the same team as taskId.", hint = "Call search_tasks to find it.")
    private String relatedTaskId;

    @NotNull
    @McpField("Read it as: taskId SUBTASK of relatedTaskId, taskId BLOCKS relatedTaskId, or taskId IS_BLOCKED_BY relatedTaskId.")
    private TaskRelationType relationType;
}
