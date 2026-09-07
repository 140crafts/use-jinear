package co.jinear.core.model.mcp.jsonrpc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The outcome of one MCP HTTP exchange: either a body to return, or nothing when the batch
 * held only notifications, which JSON-RPC answers with 202 Accepted and no body.
 */
@Getter
@AllArgsConstructor
public class McpExchange {

    private final McpJsonRpcPayload body;
    private final boolean accepted;

    public static McpExchange of(McpJsonRpcPayload body) {
        return new McpExchange(body, false);
    }

    public static McpExchange accepted() {
        return new McpExchange(null, true);
    }
}
