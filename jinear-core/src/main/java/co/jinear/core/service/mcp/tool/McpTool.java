package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import com.fasterxml.jackson.databind.JsonNode;

public interface McpTool {

    McpToolDefinition definition();

    McpToolResult call(McpToolContext context, JsonNode arguments);

    default String name() {
        return definition().getName();
    }
}
