package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetContextInput {

    @McpField("Narrow the result to this one workspace. Leave it out to get every workspace.")
    private String workspaceId;

    @McpField("Include the members of each workspace, with their account ids. Defaults to false.")
    private Boolean includeMembers;
}
