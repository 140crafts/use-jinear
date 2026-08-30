package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.mcp.McpConnectionDto;
import co.jinear.core.model.dto.mcp.McpOauthClientDto;
import co.jinear.core.model.dto.mcp.McpToolCallLogDto;
import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.entity.mcp.McpOauthClient;
import co.jinear.core.model.entity.mcp.McpToolCallLog;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class McpDtoConverter {

    public McpConnectionDto convert(McpConnection entity, Long callCountLast30Days) {
        McpConnectionDto dto = new McpConnectionDto();
        dto.setMcpConnectionId(entity.getMcpConnectionId());
        dto.setAccountId(entity.getAccountId());
        dto.setClientId(entity.getClientId());
        dto.setClientName(entity.getClientName());
        dto.setClientDisplayHost(hostOf(entity.getClientId()));
        dto.setGrantedScopes(splitScopes(entity.getGrantedScopes()));
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastUsedAt(entity.getLastUsedAt());
        dto.setCallCountLast30Days(callCountLast30Days);
        return dto;
    }

    public McpToolCallLogDto convert(McpToolCallLog entity) {
        McpToolCallLogDto dto = new McpToolCallLogDto();
        dto.setMcpToolCallLogId(entity.getMcpToolCallLogId());
        dto.setMcpConnectionId(entity.getMcpConnectionId());
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

    public McpOauthClientDto convert(McpOauthClient entity) {
        McpOauthClientDto dto = new McpOauthClientDto();
        dto.setClientId(entity.getClientId());
        dto.setClientName(entity.getClientName());
        dto.setClientUri(entity.getClientUri());
        dto.setLogoUri(entity.getLogoUri());
        dto.setRedirectUris(splitLines(entity.getRedirectUris()));
        dto.setRegistrationType(entity.getRegistrationType());
        dto.setClientIdIssuedAt(entity.getClientIdIssuedAt());
        return dto;
    }

    /**
     * A client identified by a metadata document is displayed by the host of that
     * document, never by the name inside it, because the document is self asserted.
     */
    private String hostOf(String clientId) {
        if (Objects.isNull(clientId) || !clientId.startsWith("https://")) {
            return clientId;
        }
        try {
            String host = URI.create(clientId).getHost();
            return Objects.isNull(host) ? clientId : host;
        } catch (IllegalArgumentException exception) {
            return clientId;
        }
    }

    private List<String> splitScopes(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.trim().split("\\s+")).toList();
    }

    private List<String> splitLines(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\n")).map(String::trim).filter(item -> !item.isEmpty()).toList();
    }
}
