package co.jinear.core.system.mcp;

import lombok.experimental.UtilityClass;

/**
 * Paths that sit outside the /v1 API surface and therefore need naming in more than one
 * place: the security chain, the session filter that must skip them, and the gateway
 * configuration that routes them.
 */
@UtilityClass
public class McpPaths {

    public static final String MCP_ENDPOINT = "/mcp";
    public static final String WELL_KNOWN_PREFIX = "/.well-known/";
}
