package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * One tool exposed over MCP.
 * <p>
 * Implementations delegate to the existing manager layer rather than reaching into
 * repositories, so workspace permissions, validation, localized errors and workspace
 * activity all behave exactly as they do for the app.
 */
public interface McpTool {

    McpToolDefinition definition();

    McpToolResult call(McpToolContext context, JsonNode arguments);

    default String name() {
        return definition().getName();
    }
}
