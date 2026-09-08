package co.jinear.core.manager.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.mcp.McpDisabledException;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.jsonrpc.*;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.mcp.McpProtocolService;
import co.jinear.core.service.oauth.provider.OauthAccessTokenResolver;
import co.jinear.core.validator.mcp.McpToolScopeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpManager {

    private final McpProtocolService mcpProtocolService;
    private final McpToolScopeValidator mcpToolScopeValidator;
    private final OauthAccessTokenResolver oauthAccessTokenResolver;
    private final McpProperties mcpProperties;

    public McpExchange handle(McpJsonRpcRequestBatch batch) {
        validateMcpIsEnabled();

        Optional<OauthAccessTokenVo> token = oauthAccessTokenResolver.currentAccessToken();
        mcpToolScopeValidator.validateScopes(batch, token);

        List<McpJsonRpcResponse> responses = new ArrayList<>();
        for (McpJsonRpcRequest message : batch.getMessages()) {
            mcpProtocolService.handle(message, contextFor(token)).ifPresent(responses::add);
        }

        if (responses.isEmpty()) {
            return McpExchange.accepted();
        }
        return McpExchange.of(batch.isBatch()
                ? new McpJsonRpcResponseBatch(responses)
                : responses.get(0));
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
