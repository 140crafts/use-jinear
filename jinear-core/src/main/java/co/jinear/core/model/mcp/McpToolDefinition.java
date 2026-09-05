package co.jinear.core.model.mcp;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
public class McpToolDefinition {

    private final String name;
    private final String description;
    private final ObjectNode inputSchema;
    private final ObjectNode outputSchema;
    private final McpToolAnnotations annotations;
    private final Set<OauthScope> requiredScopes;

    public String title() {
        return annotations.getTitle();
    }
}
