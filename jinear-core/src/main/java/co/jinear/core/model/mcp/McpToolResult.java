package co.jinear.core.model.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class McpToolResult {

    private final JsonNode structuredContent;
    private final String text;
    private final boolean error;

    private McpToolResult(JsonNode structuredContent, String text, boolean error) {
        this.structuredContent = structuredContent;
        this.text = text;
        this.error = error;
    }

    public static McpToolResult of(JsonNode structuredContent) {
        return new McpToolResult(structuredContent, null, false);
    }

    public static McpToolResult error(String message) {
        return new McpToolResult(null, message, true);
    }
}
