package co.jinear.core.model.mcp.view;

/**
 * Marks a class as something an MCP tool may return as its structured content. Every payload
 * an MCP tool emits is a declared class, so the wire shape and its published JSON Schema
 * cannot drift apart.
 */
public interface McpToolPayload {
}
