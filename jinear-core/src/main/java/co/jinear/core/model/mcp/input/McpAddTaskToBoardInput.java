package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAddTaskToBoardInput {

    @NotBlank
    @McpField(value = "Board id, from list_task_boards.", hint = "Call list_task_boards to find it.")
    private String taskBoardId;

    @NotBlank
    @McpField(value = "Task id, from search_tasks or list_tasks.", hint = "Call search_tasks to find it.")
    private String taskId;
}
