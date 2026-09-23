package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpFetchInput {

    @NotBlank
    @McpField(value = "An id returned by search.", hint = "Call search first and pass an id exactly as it was returned.")
    private String id;
}
