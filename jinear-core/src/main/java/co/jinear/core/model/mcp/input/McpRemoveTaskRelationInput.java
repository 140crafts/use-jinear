package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpRemoveTaskRelationInput {

    @NotBlank
    @McpField(value = "Relation id, from the relations or relatedIn of get_task.", hint = "Call get_task to find it.")
    private String taskRelationId;
}
