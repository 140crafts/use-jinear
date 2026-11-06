package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpSearchInput {

    @NotBlank
    @McpField("Keywords to find. Every word must appear in the record as a whole word or as the start of a word.")
    private String query;
}
