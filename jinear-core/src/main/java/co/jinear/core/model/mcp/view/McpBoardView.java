package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpBoardView implements McpToolPayload {

    @McpField("Board id.")
    private String taskBoardId;

    @McpField("Workspace this board belongs to.")
    private String workspaceId;

    @McpField("Team this board belongs to.")
    private String teamId;

    @McpField("Board name.")
    private String title;

    @McpField("Board lifecycle state.")
    private String state;

    @McpField("ISO 8601 due date, or null.")
    private String dueDate;
}
