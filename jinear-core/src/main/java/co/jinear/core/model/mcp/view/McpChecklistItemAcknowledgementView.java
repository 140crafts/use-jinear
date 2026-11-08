package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistItemAcknowledgementView implements McpToolPayload {

    @McpField("The checklist item the operation applied to.")
    private String checklistItemId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpChecklistItemAcknowledgementView of(String checklistItemId) {
        McpChecklistItemAcknowledgementView view = new McpChecklistItemAcknowledgementView();
        view.setChecklistItemId(checklistItemId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
