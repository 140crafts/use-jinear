package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpCreateNotebookInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField("Notebook title.")
    private String title;

    @McpField("Short description of the notebook.")
    private String description;

    @McpField("Who can see it. PRIVATE: only the creator. SHARED: the creator and the members added in the Jinear app. PUBLIC_WITHIN_WORKSPACE: every workspace member. Defaults to PUBLIC_WITHIN_WORKSPACE.")
    private NotebookVisibilityType visibility;
}
