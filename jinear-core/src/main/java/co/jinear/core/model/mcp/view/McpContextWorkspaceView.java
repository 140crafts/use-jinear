package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextWorkspaceView {

    @McpField("Workspace id. Pass this to any tool that takes workspaceId.")
    private String workspaceId;

    @McpField("Short workspace handle used in Jinear URLs.")
    private String username;

    @McpField("Display name.")
    private String title;

    @McpField("The caller's role in this workspace: OWNER, ADMIN or MEMBER.")
    private String role;

    @McpField("Billing tier, BASIC or PRO.")
    private String tier;

    @McpField("Number of people in this workspace.")
    private Long memberCount;

    @McpField("People in this workspace. Present only when includeMembers is true.")
    private List<McpContextMemberView> members;

    @McpField("Teams in this workspace the caller is a member of. Archived teams are left out.")
    private List<McpContextTeamView> teams;
}
