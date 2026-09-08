package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * The parameters of an MCP {@code initialize} call. Only the protocol version is negotiated
 * here; the client's own capabilities and info are accepted and ignored.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class McpInitializeParams {

    private String protocolVersion;
}
