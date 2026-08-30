package co.jinear.core.manager.mcp;

import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.mcp.McpOauthClientDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpOauthClientListingResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import co.jinear.core.service.mcp.oauth.McpOauthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * The instance wide view. Reached only through /v1/admin, which the security chain
 * already restricts to ROLE_ADMIN.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAdminManager {

    private static final int DEFAULT_WINDOW_DAYS = 30;
    private static final int PAGE_SIZE = 25;

    private final McpAnalyticsService mcpAnalyticsService;
    private final McpOauthClientService mcpOauthClientService;
    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpDtoConverter mcpDtoConverter;

    public McpAnalyticsResponse analytics() {
        McpAnalyticsResponse response = new McpAnalyticsResponse();
        response.setMcpAnalyticsDto(mcpAnalyticsService.summarize(null, DEFAULT_WINDOW_DAYS));
        return response;
    }

    public McpOauthClientListingResponse listClients(int page) {
        var clients = mcpOauthClientService.listClients(PageRequest.of(page, PAGE_SIZE))
                .map(mcpDtoConverter::convert);
        McpOauthClientListingResponse response = new McpOauthClientListingResponse();
        response.setMcpOauthClientDtoPage(new PageDto<McpOauthClientDto>(clients));
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

    public BaseResponse revokeClient(String clientId) {
        mcpOauthClientService.revokeClient(clientId);
        return new BaseResponse();
    }
}
