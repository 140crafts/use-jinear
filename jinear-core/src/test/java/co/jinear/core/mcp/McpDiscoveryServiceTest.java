package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.service.mcp.McpDiscoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These two documents are the whole handshake. Every field asserted here is one a client
 * reads before it will attempt an authorization, and a missing one shows up as an
 * unexplained connection failure rather than an error.
 */
@SuppressWarnings("unchecked")
class McpDiscoveryServiceTest {

    private McpProperties properties;
    private McpDiscoveryService service;

    @BeforeEach
    void setUp() {
        properties = new McpProperties();
        properties.setIssuerUrl("https://api.jinear.test");
        properties.setResourceUrl("https://api.jinear.test/mcp");
        properties.setDocumentationUrl("https://jinear.co/mcp/");
        properties.setDcrEnabled(Boolean.TRUE);
        service = new McpDiscoveryService(properties);
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
    void authorizationServerMetadataAdvertisesS256() {
        // A client that cannot confirm S256 support is required to refuse to proceed.
        assertThat(service.authorizationServerMetadata().get("code_challenge_methods_supported"))
                .isEqualTo(List.of("S256"));
    }

    @Test
    void authorizationServerMetadataEnablesClientIdMetadataDocuments() {
        Map<String, Object> document = service.authorizationServerMetadata();

        // Both of these are required together. With either missing, a client falls back to
        // dynamic registration and registers a fresh row on every single connection.
        assertThat(document.get("client_id_metadata_document_supported")).isEqualTo(Boolean.TRUE);
        assertThat(document.get("token_endpoint_auth_methods_supported")).isEqualTo(List.of("none"));
    }

    @Test
    void authorizationServerMetadataNamesEveryEndpoint() {
        Map<String, Object> document = service.authorizationServerMetadata();

        assertThat(document.get("issuer")).isEqualTo("https://api.jinear.test");
        assertThat(document.get("authorization_endpoint")).isEqualTo("https://api.jinear.test/v1/oauth/authorize");
        assertThat(document.get("token_endpoint")).isEqualTo("https://api.jinear.test/v1/oauth/token");
        assertThat(document.get("registration_endpoint")).isEqualTo("https://api.jinear.test/v1/oauth/register");
        assertThat(document.get("revocation_endpoint")).isEqualTo("https://api.jinear.test/v1/oauth/revoke");
        assertThat(document.get("grant_types_supported")).isEqualTo(List.of("authorization_code", "refresh_token"));
        assertThat(document.get("response_types_supported")).isEqualTo(List.of("code"));
    }

    @Test
    void omitsTheRegistrationEndpointWhenDynamicRegistrationIsTurnedOff() {
        properties.setDcrEnabled(Boolean.FALSE);

        assertThat(service.authorizationServerMetadata()).doesNotContainKey("registration_endpoint");
    }

    @Test
    void protectedResourceMetadataUrlUsesThePathSuffixedForm() {
        assertThat(properties.protectedResourceMetadataUrl())
                .isEqualTo("https://api.jinear.test/.well-known/oauth-protected-resource/mcp");
    }
}
