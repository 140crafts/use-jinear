package co.jinear.core.manager.mcp;

import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpServerInfoResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.mcp.McpServerInfoService;
import co.jinear.core.service.mcp.McpToolCallLogListingService;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpManagementManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;

    private final McpAnalyticsService mcpAnalyticsService;
    private final McpServerInfoService mcpServerInfoService;
    private final McpToolCallLogListingService mcpToolCallLogListingService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;

    public McpServerInfoResponse retrieveServerInfo() {
        McpServerInfoResponse response = new McpServerInfoResponse();
        response.setMcpServerInfoDto(mcpServerInfoService.retrieveServerInfo());
        return response;
    }

    public McpToolCallLogListingResponse listWorkspaceLogs(String workspaceId, int page) {
        validateWorkspaceAdmin(workspaceId);
        McpToolCallLogListingResponse response = new McpToolCallLogListingResponse();
        response.setMcpToolCallLogDtoPage(mcpToolCallLogListingService.listForWorkspace(workspaceId, page));
        return response;
    }

    public McpAnalyticsResponse workspaceAnalytics(String workspaceId) {
        validateWorkspaceAdmin(workspaceId);
        McpAnalyticsResponse response = new McpAnalyticsResponse();
        response.setMcpAnalyticsDto(mcpAnalyticsService.summarize(workspaceId, DEFAULT_WINDOW_DAYS));
        return response;
    }

    private void validateWorkspaceAdmin(String workspaceId) {
        workspaceValidator.validateHasAdminAccess(sessionInfoService.currentAccountId(), workspaceId);
    }
}
