package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpRemoveTaskFromBoardInput {

    @NotBlank
    @McpField(value = "Board entry id, from the boards of get_task.", hint = "Call get_task to find it.")
    private String taskBoardEntryId;
}
