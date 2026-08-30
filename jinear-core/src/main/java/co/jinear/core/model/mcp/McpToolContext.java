package co.jinear.core.model.mcp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * The caller behind one tools/call.
 * <p>
 * workspaceId is the one mutable field: it is not known until the tool has read its own
 * arguments, and the call log wants it so a workspace owner can see what an agent did in
 * their workspace. A tool sets it as soon as it resolves one.
 */
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
