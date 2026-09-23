package co.jinear.core.model.mcp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
public class McpToolContext {

    private final String accountId;
    private final String connectionId;
    private final String clientId;
    private final Set<String> scopes;

    @Setter
    private String workspaceId;

    public boolean isAuthenticated() {
        return accountId != null;
    }
}
