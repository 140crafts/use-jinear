package co.jinear.core.exception.mcp;

/**
 * Raised when the MCP server is switched off on this instance. The endpoint then answers 404,
 * so a disabled instance looks like one that never had the feature.
 */
public class McpDisabledException extends RuntimeException {
}
