package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;

public interface McpTool {

    McpToolDefinition definition();

    McpToolResult call(McpToolContext context, McpToolArguments arguments);

    default String name() {
        return definition().getName();
    }
}
