package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListTaskAttachmentsInput {

    @NotBlank
    @McpField(McpInputDescriptions.TASK_ID)
    private String taskId;
}
