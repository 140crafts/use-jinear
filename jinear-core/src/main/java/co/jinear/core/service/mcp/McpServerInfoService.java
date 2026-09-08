package co.jinear.core.service.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.dto.mcp.McpServerInfoDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.service.management.InstanceFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Answers whether this instance is serving MCP. All three switches have to agree: the MCP
 * feature, the OAuth provider it depends on, and the instance flag an admin controls.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpServerInfoService {

    private final McpProperties mcpProperties;
    private final OauthProperties oauthProperties;
    private final InstanceFlagService instanceFlagService;

    public McpServerInfoDto retrieveServerInfo() {
        boolean enabled = Boolean.TRUE.equals(mcpProperties.getEnabled())
                && Boolean.TRUE.equals(oauthProperties.getEnabled())
                && instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER);

        McpServerInfoDto dto = new McpServerInfoDto();
        dto.setEnabled(enabled);
        dto.setServerUrl(enabled ? mcpProperties.getResourceUrl() : null);
        dto.setDocumentationUrl(mcpProperties.getDocumentationUrl());
        return dto;
    }
}
