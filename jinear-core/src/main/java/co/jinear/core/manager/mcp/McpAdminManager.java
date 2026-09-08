package co.jinear.core.manager.mcp;

import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.service.mcp.McpToolCallLogListingService;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpAdminManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;

    private final McpAnalyticsService mcpAnalyticsService;
    private final McpToolCallLogListingService mcpToolCallLogListingService;

    public McpAnalyticsResponse analytics() {
        McpAnalyticsResponse response = new McpAnalyticsResponse();
        response.setMcpAnalyticsDto(mcpAnalyticsService.summarize(null, DEFAULT_WINDOW_DAYS));
        return response;
    }

    public McpToolCallLogListingResponse listLogs(int page) {
        McpToolCallLogListingResponse response = new McpToolCallLogListingResponse();
        response.setMcpToolCallLogDtoPage(mcpToolCallLogListingService.listAll(page));
        return response;
    }
}
