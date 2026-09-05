package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.entity.mcp.McpToolCallLog;
import org.springframework.stereotype.Component;

@Component
public class McpDtoConverter {

    public McpToolCallLogDto convert(McpToolCallLog entity) {
        McpToolCallLogDto dto = new McpToolCallLogDto();
        dto.setMcpToolCallLogId(entity.getMcpToolCallLogId());
        dto.setOauthConnectionId(entity.getOauthConnectionId());
        dto.setAccountId(entity.getAccountId());
        dto.setWorkspaceId(entity.getWorkspaceId());
        dto.setClientId(entity.getClientId());
        dto.setToolName(entity.getToolName());
        dto.setCallStatus(entity.getCallStatus());
        dto.setErrorCode(entity.getErrorCode());
        dto.setDurationMs(entity.getDurationMs());
        dto.setResponseBytes(entity.getResponseBytes());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
