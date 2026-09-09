package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetTaskInput {

    @NotBlank
    @McpField("Workspace handle, the username field from list_workspaces.")
    private String workspaceUsername;

    @NotBlank
    @McpField("Team tag, the part before the dash in a reference such as ENG-42.")
    private String teamTag;

    @NotNull
    @McpField(value = "Task number, the part after the dash in a reference such as ENG-42.",
            hint = "In the reference ENG-42 it is 42.")
    private Integer taskNumber;
}
