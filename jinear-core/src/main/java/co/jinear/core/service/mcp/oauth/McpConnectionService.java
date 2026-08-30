package co.jinear.core.service.mcp.oauth;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.repository.mcp.McpConnectionRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpConnectionService {

    private final McpConnectionRepository mcpConnectionRepository;
    private final McpScopeService mcpScopeService;
    private final SessionInfoService sessionInfoService;
    private final PassiveService passiveService;

    /**
     * One connection per account and client. Re-consenting widens the existing grant
     * rather than piling up rows, so the management screen shows one entry per client
     * and revoking it really does end that client's access.
     */
    public McpConnection grant(String accountId, String clientId, String clientName, Set<String> scopes) {
        McpConnection connection = mcpConnectionRepository
                .findFirstByAccountIdAndClientIdAndPassiveIdIsNull(accountId, clientId)
                .orElseGet(McpConnection::new);
        connection.setAccountId(accountId);
        connection.setClientId(clientId);
        connection.setClientName(clientName);
        connection.setGrantedScopes(mcpScopeService.format(scopes));
        if (Objects.isNull(connection.getSessionInfoId())) {
            // Write managers stamp the session id onto every workspace activity row, so a
            // connection opens a real session exactly as a browser login does.
            connection.setSessionInfoId(sessionInfoService.initialize(ProviderType.MCP, accountId));
        }
        McpConnection saved = mcpConnectionRepository.save(connection);
        log.info("[MCP] Granted connection. accountId: {}, clientId: {}, scopes: {}", accountId, clientId, scopes);
        return saved;
    }

    public Optional<McpConnection> retrieveOptional(String mcpConnectionId) {
        return mcpConnectionRepository.findByMcpConnectionIdAndPassiveIdIsNull(mcpConnectionId);
    }

    public McpConnection retrieve(String mcpConnectionId) {
        return retrieveOptional(mcpConnectionId).orElseThrow(NotFoundException::new);
    }

    public List<McpConnection> listForAccount(String accountId) {
        return mcpConnectionRepository.findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(accountId);
    }

    public long countActive() {
        return mcpConnectionRepository.countByPassiveIdIsNull();
    }

    public void touch(McpConnection connection) {
        connection.setLastUsedAt(DateHelper.now());
        mcpConnectionRepository.save(connection);
    }

    public String revoke(String mcpConnectionId) {
        McpConnection connection = retrieve(mcpConnectionId);
        String passiveId = passiveService.createUserActionPassive(connection.getAccountId());
        connection.setPassiveId(passiveId);
        mcpConnectionRepository.save(connection);
        log.info("[MCP] Revoked connection. mcpConnectionId: {}", mcpConnectionId);
        return passiveId;
    }
}
