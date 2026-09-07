package co.jinear.core.model.mcp.jsonrpc;

import co.jinear.core.model.mcp.view.McpToolPayload;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"content", "structuredContent", "isError"})
public class McpToolCallResult implements McpResult {

    private List<McpContentBlock> content;
    private McpToolPayload structuredContent;

    @JsonProperty("isError")
    private boolean isError;
}
