package co.jinear.core.service.mcp.analytics;

import co.jinear.core.model.dto.mcp.McpAnalyticsDto;
import co.jinear.core.model.dto.mcp.McpDailyUsageDto;
import co.jinear.core.model.dto.mcp.McpToolUsageDto;
import co.jinear.core.model.entity.mcp.McpUsageDaily;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.repository.mcp.McpUsageDailyRepository;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Usage figures for the management screens.
 * <p>
 * Per tool totals come from the raw log, which only reaches back as far as the retention
 * window. The daily series comes from the rollup table, which survives pruning, so a chart
 * keeps its history after the rows behind it are gone.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAnalyticsService {

    private static final int TOP_TOOL_LIMIT = 10;

    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpUsageDailyRepository mcpUsageDailyRepository;
    private final OauthConnectionService oauthConnectionService;

    public McpAnalyticsDto summarize(String workspaceId, int windowDays) {
        Date from = DateHelper.substractDays(DateHelper.now(), windowDays);
        List<McpToolCallLogRepository.McpToolUsageProjection> byTool =
                mcpToolCallLogRepository.summarizeByTool(from, workspaceId);

        List<McpToolUsageDto> topTools = byTool.stream()
                .map(this::toUsage)
                .sorted(Comparator.comparingLong(McpToolUsageDto::getCallCount).reversed())
                .limit(TOP_TOOL_LIMIT)
                .toList();

        long totalCalls = byTool.stream().mapToLong(McpToolCallLogRepository.McpToolUsageProjection::getCallCount).sum();
        long errorCalls = byTool.stream().mapToLong(McpToolCallLogRepository.McpToolUsageProjection::getErrorCount).sum();

        McpAnalyticsDto dto = new McpAnalyticsDto();
        dto.setWindowDays(windowDays);
        dto.setTotalCalls(totalCalls);
        dto.setErrorCalls(errorCalls);
        dto.setActiveConnections(Objects.isNull(workspaceId) ? oauthConnectionService.countActive() : null);
        dto.setTopTools(topTools);
        dto.setDaily(daily(workspaceId, windowDays));
        return dto;
    }

    private List<McpDailyUsageDto> daily(String workspaceId, int windowDays) {
        LocalDate from = LocalDate.now().minusDays(windowDays);
        List<McpUsageDaily> rows = Objects.isNull(workspaceId)
                ? mcpUsageDailyRepository.findAllByUsageDateGreaterThanEqualAndPassiveIdIsNull(from)
                : mcpUsageDailyRepository.findAllByUsageDateGreaterThanEqualAndWorkspaceIdAndPassiveIdIsNull(from, workspaceId);

        Map<LocalDate, long[]> byDate = new TreeMap<>();
        rows.forEach(row -> {
            long[] totals = byDate.computeIfAbsent(row.getUsageDate(), key -> new long[2]);
            totals[0] += Objects.isNull(row.getCallCount()) ? 0 : row.getCallCount();
            totals[1] += Objects.isNull(row.getErrorCount()) ? 0 : row.getErrorCount();
        });

        List<McpDailyUsageDto> series = new ArrayList<>();
        byDate.forEach((date, totals) -> {
            McpDailyUsageDto dto = new McpDailyUsageDto();
            dto.setDate(date.toString());
            dto.setCallCount(totals[0]);
            dto.setErrorCount(totals[1]);
            series.add(dto);
        });
        return series;
    }

    private McpToolUsageDto toUsage(McpToolCallLogRepository.McpToolUsageProjection projection) {
        McpToolUsageDto dto = new McpToolUsageDto();
        dto.setToolName(projection.getToolName());
        dto.setCallCount(projection.getCallCount());
        dto.setErrorCount(projection.getErrorCount());
        long calls = Objects.isNull(projection.getCallCount()) ? 0 : projection.getCallCount();
        long duration = Objects.isNull(projection.getTotalDurationMs()) ? 0 : projection.getTotalDurationMs();
        dto.setAverageDurationMs(calls == 0 ? 0 : duration / calls);
        return dto;
    }
}
