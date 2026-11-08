package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class McpListActivityInput extends McpWorkspaceScopedInput {

    @McpField("Restrict to these teams, from list_teams. Leave out for every team the account can see.")
    private List<String> teamIds;

    @McpField("Restrict to these tasks.")
    private List<String> taskIds;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;

    @McpField(value = McpInputDescriptions.PAGE_SIZE, minimum = 1)
    private Integer pageSize;
}
