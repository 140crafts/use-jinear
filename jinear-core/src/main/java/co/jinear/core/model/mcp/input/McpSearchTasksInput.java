package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class McpSearchTasksInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField("Free text to match against task titles and bodies.")
    private String query;

    @McpField("Restrict the search to these teams. Omit to search the whole workspace.")
    private List<String> teamIds;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
