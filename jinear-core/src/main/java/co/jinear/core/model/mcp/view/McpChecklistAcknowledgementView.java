package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistAcknowledgementView implements McpToolPayload {

    @McpField("The checklist the operation applied to.")
    private String checklistId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpChecklistAcknowledgementView of(String checklistId) {
        McpChecklistAcknowledgementView view = new McpChecklistAcknowledgementView();
        view.setChecklistId(checklistId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
