package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNotebookAcknowledgementView implements McpToolPayload {

    @McpField("Id of the created notebook.")
    private String notebookId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpNotebookAcknowledgementView of(String notebookId) {
        McpNotebookAcknowledgementView view = new McpNotebookAcknowledgementView();
        view.setNotebookId(notebookId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
