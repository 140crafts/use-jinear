package co.jinear.core.model.mcp.jsonrpc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class McpServerCapabilities {

    private McpToolsCapability tools;
}
