package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTeamView implements McpToolPayload {

    @McpField("Team id. Required by every task tool.")
    private String teamId;

    @McpField("Workspace this team belongs to.")
    private String workspaceId;

    @McpField("Display name.")
    private String name;

    @McpField("Short team handle used in Jinear URLs.")
    private String username;

    @McpField("Short prefix used in task references, as in ENG-42.")
    private String tag;

    @McpField("Who can see this team's tasks.")
    private String taskVisibility;

    @McpField("ACTIVE or ARCHIVED.")
    private String teamState;
}
