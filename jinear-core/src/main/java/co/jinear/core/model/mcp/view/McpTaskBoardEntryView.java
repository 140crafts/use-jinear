package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskBoardEntryView implements McpToolPayload {

    @McpField("Board entry id. Pass it to remove_task_from_board or move_task_on_board.")
    private String taskBoardEntryId;

    @McpField("Board id.")
    private String taskBoardId;

    @McpField("Board title.")
    private String title;

    @McpField("Position of the task on the board. A lower order comes first.")
    private Integer order;
}
