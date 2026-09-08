package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.response.oauth.OauthProtectedResourceMetadataResponse;
import co.jinear.core.service.mcp.McpDiscoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class McpDiscoveryServiceTest {

    private McpDiscoveryService service;

    @BeforeEach
    void setUp() {
        OauthProperties oauthProperties = new OauthProperties();
        oauthProperties.setIssuerUrl("https://api.jinear.test");
        McpProperties mcpProperties = new McpProperties();
        mcpProperties.setResourceUrl("https://api.jinear.test/mcp");
        mcpProperties.setDocumentationUrl("https://jinear.co/mcp/");
        service = new McpDiscoveryService(mcpProperties, oauthProperties);
    }

    @Test
    void protectedResourceMetadataNamesTheResourceAndItsAuthorizationServer() {
        OauthProtectedResourceMetadataResponse document = service.protectedResourceMetadata();

        assertThat(document.getResource()).isEqualTo("https://api.jinear.test/mcp");
        assertThat(document.getAuthorizationServers()).isEqualTo(List.of("https://api.jinear.test"));
        assertThat(document.getBearerMethodsSupported()).isEqualTo(List.of("header"));
        assertThat(document.getScopesSupported())
                .contains("tasks:read", "tasks:write", "offline_access");
    }

    @Test
    void protectedResourceMetadataUrlUsesThePathSuffixedForm() {
        assertThat(service.protectedResourceMetadataUrl())
                .isEqualTo("https://api.jinear.test/.well-known/oauth-protected-resource/mcp");
    }
}
