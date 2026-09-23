package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"name", "title", "version"})
public class McpServerInfo {

    private String name;
    private String title;
    private String version;
}
