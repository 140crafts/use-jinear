package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListTaskBoardsInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = McpInputDescriptions.TEAM_ID, hint = "Call list_teams to find it.")
    private String teamId;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
