package co.jinear.core.model.response.mcp;

import co.jinear.core.model.mcp.McpToolDescriptor;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonPropertyOrder({"tools", "scopes"})
public class McpToolManifestResponse {

    private List<McpToolDescriptor> tools;
    private Map<String, List<String>> scopes;
}
