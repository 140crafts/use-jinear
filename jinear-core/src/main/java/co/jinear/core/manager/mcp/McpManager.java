package co.jinear.core.manager.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.mcp.McpDisabledException;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.jsonrpc.*;
import co.jinear.core.manager.mcp.tool.McpToolRegistry;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.manager.mcp.McpProtocolManager;
import co.jinear.core.service.oauth.provider.OauthAccessTokenResolver;
import co.jinear.core.validator.mcp.McpToolScopeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpManager {

    private final McpProtocolManager mcpProtocolManager;
    private final McpToolScopeValidator mcpToolScopeValidator;
    private final McpToolRegistry mcpToolRegistry;
    private final OauthAccessTokenResolver oauthAccessTokenResolver;
    private final McpProperties mcpProperties;

    public McpExchange handle(McpJsonRpcRequestBatch batch) {
        validateMcpIsEnabled();

        Optional<OauthAccessTokenVo> token = oauthAccessTokenResolver.currentAccessToken();
        validateScopes(batch, token);

        List<McpJsonRpcResponse> responses = new ArrayList<>();
        for (McpJsonRpcRequest message : batch.getMessages()) {
            mcpProtocolManager.handle(message, contextFor(token)).ifPresent(responses::add);
        }

        if (responses.isEmpty()) {
            return McpExchange.accepted();
        }
        return McpExchange.of(batch.isBatch()
                ? new McpJsonRpcResponseBatch(responses)
                : responses.get(0));
    }

    /**
     * A batch is refused as a whole if any tool call in it is short of a scope, so nothing is
     * half executed.
     */
    private void validateScopes(McpJsonRpcRequestBatch batch, Optional<OauthAccessTokenVo> token) {
        for (McpJsonRpcRequest message : batch.getMessages()) {
            if (!mcpProtocolManager.isToolCall(message)) {
                continue;
            }
            String toolName = mcpProtocolManager.toolNameOf(message);
            if (Objects.isNull(toolName)) {
                continue;
            }
            mcpToolRegistry.find(toolName).ifPresent(tool ->
                    mcpToolScopeValidator.validateScopes(toolName, tool.definition().getRequiredScopes(), token));
        }
    }

    private McpToolContext contextFor(Optional<OauthAccessTokenVo> token) {
        return token.map(vo -> McpToolContext.builder()
                        .accountId(vo.getAccountId())
                        .connectionId(vo.getConnectionId())
                        .clientId(vo.getClientId())
                        .scopes(vo.getScopes())
                        .build())
                .orElseGet(() -> McpToolContext.builder().scopes(Set.of()).build());
    }

    private void validateMcpIsEnabled() {
        if (!Boolean.TRUE.equals(mcpProperties.getEnabled())) {
            throw new McpDisabledException();
        }
    }
}
