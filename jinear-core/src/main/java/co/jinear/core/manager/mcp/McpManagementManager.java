package co.jinear.core.manager.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.mcp.McpServerInfoDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpServerInfoResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * What a member can learn about the MCP server itself, plus the workspace scoped tool
 * call view a workspace owner gets. The connections a person has granted are managed
 * one layer down, in
 * {@link co.jinear.core.manager.oauth.provider.OauthConnectionManager}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpManagementManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;
    private static final int PAGE_SIZE = 25;

    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpAnalyticsService mcpAnalyticsService;
    private final McpDtoConverter mcpDtoConverter;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final InstanceFlagService instanceFlagService;
    private final McpProperties mcpProperties;

    /**
     * Both switches have to agree. The property decides whether the server exists at all,
     * and the instance flag is what an administrator turns on afterwards, so reporting
     * either one alone would offer a member a URL that cannot be connected.
     */
    public McpServerInfoResponse retrieveServerInfo() {
        boolean enabled = Boolean.TRUE.equals(mcpProperties.getEnabled())
                && instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER);
        McpServerInfoDto dto = new McpServerInfoDto();
        dto.setEnabled(enabled);
        dto.setServerUrl(enabled ? mcpProperties.getResourceUrl() : null);
        dto.setDocumentationUrl(mcpProperties.getDocumentationUrl());
        McpServerInfoResponse response = new McpServerInfoResponse();
        response.setMcpServerInfoDto(dto);
        return response;
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
