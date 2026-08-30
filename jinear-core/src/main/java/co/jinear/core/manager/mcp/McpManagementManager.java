package co.jinear.core.manager.mcp;

import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.mcp.McpConnectionDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpConnectionListingResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import co.jinear.core.service.mcp.oauth.McpConnectionService;
import co.jinear.core.service.mcp.oauth.McpRefreshTokenService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * What a person can see and change about their own MCP connections, plus the workspace
 * scoped view a workspace owner gets.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpManagementManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;
    private static final int PAGE_SIZE = 25;

    private final McpConnectionService mcpConnectionService;
    private final McpRefreshTokenService mcpRefreshTokenService;
    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpAnalyticsService mcpAnalyticsService;
    private final McpDtoConverter mcpDtoConverter;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;

    public McpConnectionListingResponse listMyConnections() {
        String accountId = sessionInfoService.currentAccountId();
        List<McpConnectionDto> connections = mcpConnectionService.listForAccount(accountId).stream()
                .map(connection -> mcpDtoConverter.convert(connection, null))
                .toList();
        McpConnectionListingResponse response = new McpConnectionListingResponse();
        response.setMcpConnectionDtoList(connections);
        return response;
    }

    /**
     * Revoking is deliberately owner only, not admin only: a connection is a grant one
     * person made from their own account, and nobody else's role should let them speak
     * for it.
     */
    public BaseResponse revokeConnection(String mcpConnectionId) {
        String accountId = sessionInfoService.currentAccountId();
        McpConnection connection = mcpConnectionService.retrieve(mcpConnectionId);
        if (!accountId.equalsIgnoreCase(connection.getAccountId())) {
            log.warn("[MCP] Refusing to revoke a connection belonging to another account. accountId: {}", accountId);
            throw new NoAccessException();
        }
        mcpRefreshTokenService.revokeAllForConnection(mcpConnectionId);
        mcpConnectionService.revoke(mcpConnectionId);
        return new BaseResponse();
    }

    public McpToolCallLogListingResponse listWorkspaceLogs(String workspaceId, int page) {
        assertWorkspaceAdmin(workspaceId);
        var logs = mcpToolCallLogRepository
                .findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, PageRequest.of(page, PAGE_SIZE))
                .map(mcpDtoConverter::convert);
        McpToolCallLogListingResponse response = new McpToolCallLogListingResponse();
        response.setMcpToolCallLogDtoPage(new PageDto<McpToolCallLogDto>(logs));
        return response;
    }

    public McpAnalyticsResponse workspaceAnalytics(String workspaceId) {
        assertWorkspaceAdmin(workspaceId);
        McpAnalyticsResponse response = new McpAnalyticsResponse();
        response.setMcpAnalyticsDto(mcpAnalyticsService.summarize(workspaceId, DEFAULT_WINDOW_DAYS));
        return response;
    }

    private void assertWorkspaceAdmin(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        if (!workspaceValidator.isWorkspaceAdminOrOwner(accountId, workspaceId)) {
            throw new NoAccessException();
        }
    }
}
