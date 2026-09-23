package co.jinear.core.model.mcp;

import co.jinear.core.model.mcp.view.McpToolPayload;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class McpToolResult {

    private final McpToolPayload structuredContent;
    private final String text;
    private final boolean error;

    private McpToolResult(McpToolPayload structuredContent, String text, boolean error) {
        this.structuredContent = structuredContent;
        this.text = text;
        this.error = error;
    }

    public static McpToolResult of(McpToolPayload structuredContent) {
        return new McpToolResult(structuredContent, null, false);
    }

    public static McpToolResult error(String message) {
        return new McpToolResult(null, message, true);
    }
}
