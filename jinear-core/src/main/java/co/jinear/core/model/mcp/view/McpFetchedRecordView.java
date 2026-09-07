package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpFetchedRecordView implements McpToolPayload {

    @McpField("The id that was fetched.")
    private String id;

    @McpField("Record title.")
    private String title;

    @McpField("Record body.")
    private String text;

    @McpField("Absolute Jinear URL a person can open.")
    private String url;
}
