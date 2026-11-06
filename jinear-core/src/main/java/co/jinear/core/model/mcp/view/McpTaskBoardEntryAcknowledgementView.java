package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskBoardEntryAcknowledgementView implements McpToolPayload {

    @McpField("The board entry the operation applied to.")
    private String taskBoardEntryId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskBoardEntryAcknowledgementView of(String taskBoardEntryId) {
        McpTaskBoardEntryAcknowledgementView view = new McpTaskBoardEntryAcknowledgementView();
        view.setTaskBoardEntryId(taskBoardEntryId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
