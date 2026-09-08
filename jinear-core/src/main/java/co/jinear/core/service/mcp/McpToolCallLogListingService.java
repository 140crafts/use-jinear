package co.jinear.core.service.mcp;

import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Reads the MCP tool call audit log. {@code McpToolCallLogService} writes it; this side reads
 * it, so a manager never needs the repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpToolCallLogListingService {

    private static final int PAGE_SIZE = 25;

    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpDtoConverter mcpDtoConverter;

    public PageDto<McpToolCallLogDto> listAll(int page) {
        return new PageDto<>(mcpToolCallLogRepository
                .findAllByPassiveIdIsNullOrderByCreatedDateDesc(PageRequest.of(page, PAGE_SIZE))
                .map(mcpDtoConverter::convert));
    }

    public PageDto<McpToolCallLogDto> listForWorkspace(String workspaceId, int page) {
        return new PageDto<>(mcpToolCallLogRepository
                .findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, PageRequest.of(page, PAGE_SIZE))
                .map(mcpDtoConverter::convert));
    }
}
