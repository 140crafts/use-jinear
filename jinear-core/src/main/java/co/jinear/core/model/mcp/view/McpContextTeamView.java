package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextTeamView {

    @McpField("Team id. Required by every task tool.")
    private String teamId;

    @McpField("Display name.")
    private String name;

    @McpField("Short prefix used in task references, as in ENG-42.")
    private String tag;

    @McpField("The caller's role in this team.")
    private String role;

    @McpField("Statuses this team's tasks can be in, in board order: BACKLOG, NOT_STARTED, STARTED, COMPLETED, CANCELLED.")
    private List<McpContextWorkflowStatusView> statuses;

    @McpField("Topics this team uses to label its tasks.")
    private List<McpContextTopicView> topics;
}
