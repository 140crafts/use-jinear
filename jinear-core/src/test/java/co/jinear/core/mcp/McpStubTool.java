package co.jinear.core.mcp;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;

import java.util.function.BiFunction;

final class McpStubTool implements McpTool {

    private final McpToolDefinition definition;
    private final BiFunction<McpToolContext, McpToolArguments, McpToolResult> handler;

    McpStubTool(McpToolDefinition definition, BiFunction<McpToolContext, McpToolArguments, McpToolResult> handler) {
        this.definition = definition;
        this.handler = handler;
    }

    @Override
    public McpToolDefinition definition() {
        return definition;
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments arguments) {
        return handler.apply(context, arguments);
    }
}
