package co.jinear.core.model.mcp.jsonrpc;

/**
 * Marks a class as the {@code result} of a JSON-RPC response. One implementation per MCP
 * method, so a response body is never an untyped tree.
 */
public interface McpResult {
}
