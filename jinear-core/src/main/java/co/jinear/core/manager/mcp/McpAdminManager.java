package co.jinear.core.manager.mcp;

import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * The instance wide tool call view. Reached only through /v1/admin, which the security
 * chain already restricts to ROLE_ADMIN. Registered clients are listed one layer down,
 * in {@link co.jinear.core.manager.oauth.provider.OauthAdminManager}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAdminManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;
    private static final int PAGE_SIZE = 25;

    private final McpAnalyticsService mcpAnalyticsService;
    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpDtoConverter mcpDtoConverter;

    public McpAnalyticsResponse analytics() {
        McpAnalyticsResponse response = new McpAnalyticsResponse();
        response.setMcpAnalyticsDto(mcpAnalyticsService.summarize(null, DEFAULT_WINDOW_DAYS));
        return response;
    }

    public McpToolCallLogListingResponse listLogs(int page) {
        var logs = mcpToolCallLogRepository
                .findAllByPassiveIdIsNullOrderByCreatedDateDesc(PageRequest.of(page, PAGE_SIZE))
                .map(mcpDtoConverter::convert);
        McpToolCallLogListingResponse response = new McpToolCallLogListingResponse();
        response.setMcpToolCallLogDtoPage(new PageDto<McpToolCallLogDto>(logs));
        return response;
    }
}
