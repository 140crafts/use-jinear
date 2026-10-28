package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpContextDefaultsView {

    @McpField("Default workspace id, or null when none is set.")
    private String workspaceId;

    @McpField("Default team id, or null when none is set.")
    private String teamId;
}
