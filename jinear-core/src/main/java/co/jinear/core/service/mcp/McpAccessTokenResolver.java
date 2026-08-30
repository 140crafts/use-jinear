package co.jinear.core.service.mcp;

import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.vo.mcp.McpAccessTokenVo;
import co.jinear.core.service.mcp.oauth.McpConnectionService;
import co.jinear.core.system.mcp.McpTokenHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Turns a bearer string into a usable caller identity, or nothing.
 * <p>
 * Signature and audience come from the token itself, but revocation cannot: a user who
 * disconnects a client expects that to take effect now, not when the access token
 * expires an hour later. So every call also confirms the connection is still live.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAccessTokenResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final McpTokenHelper mcpTokenHelper;
    private final McpConnectionService mcpConnectionService;

    public Optional<String> extractBearer(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }

    /**
     * "Last used" only needs to be roughly right, and a write on every tool call would put
     * a row update in front of every read. Anything older than five minutes is refreshed.
     */
    private void touchIfStale(McpConnection connection) {
        Date lastUsedAt = connection.getLastUsedAt();
        if (Objects.isNull(lastUsedAt) || lastUsedAt.before(DateHelper.substractMinutes(DateHelper.now(), 5))) {
            mcpConnectionService.touch(connection);
        }
    }

    public Optional<McpAccessTokenVo> resolve(String token) {
        Optional<McpAccessTokenVo> parsed = mcpTokenHelper.parseAccessToken(token);
        if (parsed.isEmpty()) {
            return Optional.empty();
        }
        McpAccessTokenVo vo = parsed.get();
        Optional<McpConnection> connection = mcpConnectionService.retrieveOptional(vo.getConnectionId());
        if (connection.isEmpty()) {
            log.info("[MCP] Token presented for a revoked or unknown connection: {}", vo.getConnectionId());
            return Optional.empty();
        }
        vo.setSessionInfoId(connection.get().getSessionInfoId());
        touchIfStale(connection.get());
        return Optional.of(vo);
    }
}
