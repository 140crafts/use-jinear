package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpMemberView implements McpToolPayload {

    @McpField("Membership id.")
    private String workspaceMemberId;

    @McpField("Account id. This is the value assignee fields take.")
    private String accountId;

    @McpField("OWNER, ADMIN or MEMBER.")
    private String role;

    @McpField("Account handle.")
    private String username;

    @McpField("Account email.")
    private String email;
}
