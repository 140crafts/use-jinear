package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskRelationView implements McpToolPayload {

    @McpField("Relation id. Pass it to remove_task_relation.")
    private String taskRelationId;

    @McpField("SUBTASK. Read it as: taskId SUBTASK of relatedTaskId.")
    private String relationType;

    @McpField("The task on the left of the relation.")
    private String taskId;

    @McpField("Title of taskId.")
    private String taskTitle;

    @McpField("The task on the right of the relation.")
    private String relatedTaskId;

    @McpField("Title of relatedTaskId.")
    private String relatedTaskTitle;
}
