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
    private final Object[] arguments;

    private McpToolResult(McpToolPayload structuredContent, String text, boolean error, Object[] arguments) {
        this.structuredContent = structuredContent;
        this.text = text;
        this.error = error;
        this.arguments = arguments;
    }

    public static McpToolResult of(McpToolPayload structuredContent) {
        return new McpToolResult(structuredContent, null, false, new Object[0]);
    }

    public static McpToolResult error(String message, Object... arguments) {
        return new McpToolResult(null, message, true, arguments);
    }
}
