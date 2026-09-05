package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.service.mcp.McpDiscoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Half the handshake. Every field asserted here is one a client reads before it will
 * attempt an authorization, and a missing one shows up as an unexplained connection
 * failure rather than an error. The other half is
 * {@link co.jinear.core.oauth.OauthDiscoveryServiceTest}.
 */
@SuppressWarnings("unchecked")
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
        Map<String, Object> document = service.protectedResourceMetadata();

        assertThat(document.get("resource")).isEqualTo("https://api.jinear.test/mcp");
        assertThat(document.get("authorization_servers")).isEqualTo(List.of("https://api.jinear.test"));
        assertThat(document.get("bearer_methods_supported")).isEqualTo(List.of("header"));
        assertThat((List<String>) document.get("scopes_supported"))
                .contains("tasks:read", "tasks:write", "offline_access");
    }

    @Test
    void protectedResourceMetadataUrlUsesThePathSuffixedForm() {
        assertThat(service.protectedResourceMetadataUrl())
                .isEqualTo("https://api.jinear.test/.well-known/oauth-protected-resource/mcp");
    }
}
