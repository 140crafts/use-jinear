package co.jinear.core.service.mcp.analytics;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.model.entity.mcp.McpUsageDaily;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.repository.mcp.McpUsageDailyRepository;
import co.jinear.core.service.oauth.provider.OauthAuthorizationCodeService;
import co.jinear.core.service.oauth.provider.OauthAuthorizationRequestService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Rolls yesterday's call log into the daily table, then prunes the log.
 * <p>
 * The order matters: the rollup has to be written before the rows it summarizes are
 * deleted, or a chart loses the day it was about to gain.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpRetentionService {

    private final McpToolCallLogRepository mcpToolCallLogRepository;
    private final McpUsageDailyRepository mcpUsageDailyRepository;
    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;
    private final McpProperties mcpProperties;

    @Transactional
    public void rollUpYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if (mcpUsageDailyRepository.existsByUsageDate(yesterday)) {
            return;
        }
        Date from = DateHelper.substractDays(DateHelper.now(), 1);
        List<McpToolCallLogRepository.McpToolUsageProjection> byTool =
                mcpToolCallLogRepository.summarizeByTool(from, null);
        byTool.forEach(projection -> {
            McpUsageDaily row = new McpUsageDaily();
            row.setUsageDate(yesterday);
            row.setToolName(projection.getToolName());
            row.setCallCount(nullSafe(projection.getCallCount()));
            row.setErrorCount(nullSafe(projection.getErrorCount()));
            row.setTotalDurationMs(nullSafe(projection.getTotalDurationMs()));
            mcpUsageDailyRepository.save(row);
        });
        log.info("[MCP] Rolled up {} tool rows for {}.", byTool.size(), yesterday);
    }

    @Transactional
    public void pruneExpired() {
        Date logCutoff = DateHelper.substractDays(DateHelper.now(), mcpProperties.getLogRetentionDays());
        int prunedLogs = mcpToolCallLogRepository.deleteAllOlderThan(logCutoff);

        Date oauthCutoff = DateHelper.substractDays(DateHelper.now(), 1);
        int prunedCodes = oauthAuthorizationCodeService.purgeExpiredBefore(oauthCutoff);
        int prunedRequests = oauthAuthorizationRequestService.purgeExpiredBefore(oauthCutoff);

        if (prunedLogs + prunedCodes + prunedRequests > 0) {
            log.info("[MCP] Pruned {} call logs, {} authorization codes and {} pending authorization requests.",
                    prunedLogs, prunedCodes, prunedRequests);
        }
    }

    private long nullSafe(Long value) {
        return Objects.isNull(value) ? 0L : value;
    }
}
