package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpCreateTopicInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = McpInputDescriptions.TEAM_ID, hint = "Call list_teams to find it.")
    private String teamId;

    @NotBlank
    @McpField("Topic name, for example Bug or Marketing.")
    private String name;

    @NotBlank
    @McpField("Short tag that prefixes the topic's task numbers, for example BUG. Letters and digits only; it must be unique in the workspace.")
    private String tag;

    @Size(max = 6)
    @McpField("Color as a hex value without #, for example FF6600.")
    private String color;
}
