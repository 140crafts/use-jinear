package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpSearchHitView implements McpToolPayload {

    @McpField("Opaque id to pass to fetch.")
    private String id;

    @McpField("Record title.")
    private String title;

    @McpField("Absolute Jinear URL a person can open.")
    private String url;
}
