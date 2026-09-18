package co.jinear.core.service.mcp;

import co.jinear.core.model.entity.mcp.McpToolCallLog;
import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpToolCallLogService {

    private final McpToolCallLogRepository mcpToolCallLogRepository;

    @Async
    public void recordOutcome(McpToolContext context, String toolName, boolean isError,
                              String errorCode, long durationMs, int responseBytes) {
        McpToolCallStatus status = isError ? McpToolCallStatus.TOOL_ERROR : McpToolCallStatus.OK;
        write(context, toolName, status, errorCode, durationMs, responseBytes);
    }

    @Async
    public void recordFailure(McpToolContext context, String toolName, RuntimeException exception, long durationMs) {
        write(context, toolName, McpToolCallStatus.SERVER_ERROR, exception.getClass().getSimpleName(), durationMs, 0);
    }

    @Async
    public void recordRejection(String accountId, String connectionId, String clientId,
                                String toolName, McpToolCallStatus status) {
        McpToolCallLog entity = new McpToolCallLog();
        entity.setAccountId(accountId);
        entity.setOauthConnectionId(connectionId);
        entity.setClientId(clientId);
        entity.setToolName(toolName);
        entity.setCallStatus(status);
        entity.setDurationMs(0L);
        save(entity);
    }

    private void write(McpToolContext context, String toolName, McpToolCallStatus status,
                       String errorCode, long durationMs, int responseBytes) {
        McpToolCallLog entity = new McpToolCallLog();
        if (Objects.nonNull(context)) {
            entity.setAccountId(context.getAccountId());
            entity.setOauthConnectionId(context.getConnectionId());
            entity.setClientId(context.getClientId());
            entity.setWorkspaceId(context.getWorkspaceId());
        }
        entity.setToolName(toolName);
        entity.setCallStatus(status);
        entity.setErrorCode(errorCode);
        entity.setDurationMs(durationMs);
        entity.setResponseBytes(responseBytes);
        save(entity);
    }

    private void save(McpToolCallLog entity) {
        try {
            mcpToolCallLogRepository.save(entity);
        } catch (RuntimeException exception) {
            log.warn("[MCP] Could not write a tool call log row: {}", exception.getMessage());
        }
    }
}
