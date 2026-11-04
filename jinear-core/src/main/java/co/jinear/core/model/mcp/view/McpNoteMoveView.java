package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteMoveView implements McpToolPayload {

    @McpField("Id of the note that moved.")
    private String noteId;

    @McpField("Link that opens the note at its new place in the Jinear app.")
    private String url;

    @McpField("True when the move completed.")
    private Boolean ok;
}
