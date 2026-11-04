package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpMoveTaskOnBoardInput {

    @NotBlank
    @McpField(value = "Board entry id, from the boards of get_task.", hint = "Call get_task to find it.")
    private String taskBoardEntryId;

    @NotNull
    @McpField("New position on the board. Read the current positions with get_task, or list the board with list_tasks and sort TASK_BOARD_ORDER.")
    private Integer newOrder;
}
