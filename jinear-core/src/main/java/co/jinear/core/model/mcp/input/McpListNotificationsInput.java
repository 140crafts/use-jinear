package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListNotificationsInput extends McpWorkspaceScopedInput {

    @McpField("Restrict to one team, from list_teams. Leave out for the whole workspace.")
    private String teamId;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
