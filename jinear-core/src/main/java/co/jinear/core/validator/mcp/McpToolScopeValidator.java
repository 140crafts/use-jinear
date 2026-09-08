package co.jinear.core.validator.mcp;

import co.jinear.core.exception.mcp.McpAuthChallengeException;
import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.service.mcp.McpToolCallLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Checks that a bearer token carries every scope a tool declares, records the rejection, and
 * raises the RFC 6750 challenge the caller should answer.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class McpToolScopeValidator {

    private static final String ERROR_INVALID_TOKEN = "invalid_token";
    private static final String ERROR_INSUFFICIENT_SCOPE = "insufficient_scope";
    private static final String UNAUTHORIZED_DESCRIPTION = "Authentication is required for this tool.";
    private static final String FORBIDDEN_DESCRIPTION = "This tool needs a permission that was not granted.";

    private final McpToolCallLogService mcpToolCallLogService;
    private final McpDiscoveryService mcpDiscoveryService;

    public void validateScopes(String toolName, Set<OauthScope> requiredScopes, Optional<OauthAccessTokenVo> token) {
        if (requiredScopes.isEmpty()) {
            return;
        }
        if (token.isEmpty()) {
            mcpToolCallLogService.recordRejection(null, null, null, toolName, McpToolCallStatus.UNAUTHORIZED);
            throw new McpAuthChallengeException(ERROR_INVALID_TOKEN, UNAUTHORIZED_DESCRIPTION,
                    unauthorizedChallenge(requiredScopes), false);
        }
        Set<String> granted = token.get().getScopes();
        if (grantsAll(granted, requiredScopes)) {
            return;
        }
        mcpToolCallLogService.recordRejection(token.get().getAccountId(), token.get().getConnectionId(),
                token.get().getClientId(), toolName, McpToolCallStatus.FORBIDDEN);
        throw new McpAuthChallengeException(ERROR_INSUFFICIENT_SCOPE, FORBIDDEN_DESCRIPTION,
                insufficientScopeChallenge(granted, requiredScopes), true);
    }

    private boolean grantsAll(Set<String> granted, Set<OauthScope> required) {
        return required.stream().map(OauthScope::getValue).allMatch(granted::contains);
    }

    private String unauthorizedChallenge(Set<OauthScope> required) {
        Set<String> scopes = new LinkedHashSet<>();
        required.forEach(scope -> scopes.add(scope.getValue()));
        return "Bearer error=\"" + ERROR_INVALID_TOKEN + "\", "
                + "error_description=\"" + UNAUTHORIZED_DESCRIPTION + "\", "
                + "resource_metadata=\"" + mcpDiscoveryService.protectedResourceMetadataUrl() + "\", "
                + "scope=\"" + String.join(" ", scopes) + "\"";
    }

    private String insufficientScopeChallenge(Set<String> granted, Set<OauthScope> required) {
        Set<String> scopes = new LinkedHashSet<>(granted);
        required.forEach(scope -> scopes.add(scope.getValue()));
        return "Bearer error=\"" + ERROR_INSUFFICIENT_SCOPE + "\", "
                + "scope=\"" + String.join(" ", scopes) + "\", "
                + "resource_metadata=\"" + mcpDiscoveryService.protectedResourceMetadataUrl() + "\", "
                + "error_description=\"" + FORBIDDEN_DESCRIPTION + "\"";
    }
}
