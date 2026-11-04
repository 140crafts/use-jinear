package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskRelationAcknowledgementView implements McpToolPayload {

    @McpField("The relation the operation applied to.")
    private String taskRelationId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskRelationAcknowledgementView of(String taskRelationId) {
        McpTaskRelationAcknowledgementView view = new McpTaskRelationAcknowledgementView();
        view.setTaskRelationId(taskRelationId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
