package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpWorkflowStatusView implements McpToolPayload {

    @McpField("Status id. This is what set_task_status takes.")
    private String workflowStatusId;

    @McpField("Team this status belongs to.")
    private String teamId;

    @McpField("Display name, for example In Progress.")
    private String name;

    @McpField("BACKLOG, NOT_STARTED, STARTED, COMPLETED or CANCELLED.")
    private String stateGroup;

    @McpField("Position in the team's board, left to right.")
    private Integer order;
}
