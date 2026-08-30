package co.jinear.core.model.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

/**
 * A tools/call outcome.
 * <p>
 * A business failure is returned here with isError set, not thrown as a JSON-RPC error:
 * the specification reserves protocol errors for malformed requests, and a client is
 * told to hand tool execution errors back to the model so it can correct itself.
 */
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

    /**
     * @param message must say what was wrong and what to try instead. A bare
     *                "Bad Request" fails directory review and gives the model nothing
     *                to act on.
     */
    public static McpToolResult error(String message) {
        return new McpToolResult(null, message, true);
    }
}
