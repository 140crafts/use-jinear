package co.jinear.core.service.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.model.enumtype.mcp.McpScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The two discovery documents an MCP client reads before it can authorize.
 * <p>
 * Both are plain maps rather than typed responses so they serialize exactly as the
 * RFCs specify, and so they stay out of the generated frontend type file.
 */
@Service
@RequiredArgsConstructor
public class McpDiscoveryService {

    private final McpProperties mcpProperties;

    /**
     * RFC 9728 Protected Resource Metadata.
     * <p>
     * The {@code resource} value must equal the MCP server URL exactly as a user types
     * it into their client, including the path, or the client refuses the token it
     * later receives.
     */
    public Map<String, Object> protectedResourceMetadata() {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("resource", mcpProperties.getResourceUrl());
        document.put("authorization_servers", List.of(mcpProperties.getIssuerUrl()));
        document.put("scopes_supported", List.copyOf(McpScope.allValues()));
        document.put("bearer_methods_supported", List.of("header"));
        document.put("resource_documentation", mcpProperties.getDocumentationUrl());
        return document;
    }

    /**
     * RFC 8414 Authorization Server Metadata.
     * <p>
     * Two fields decide how Claude registers itself: a client identified by a metadata
     * document is only used when {@code client_id_metadata_document_supported} is true
     * AND {@code "none"} appears in the token endpoint auth methods, because that
     * client authenticates as a public client with PKCE and no secret. Drop either and
     * every connection falls back to dynamic registration.
     */
    public Map<String, Object> authorizationServerMetadata() {
        String issuer = mcpProperties.getIssuerUrl();
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("issuer", issuer);
        document.put("authorization_endpoint", issuer + "/v1/oauth/authorize");
        document.put("token_endpoint", issuer + "/v1/oauth/token");
        document.put("revocation_endpoint", issuer + "/v1/oauth/revoke");
        if (Boolean.TRUE.equals(mcpProperties.getDcrEnabled())) {
            document.put("registration_endpoint", issuer + "/v1/oauth/register");
        }
        document.put("scopes_supported", List.copyOf(McpScope.allValues()));
        document.put("response_types_supported", List.of("code"));
        document.put("grant_types_supported", List.of("authorization_code", "refresh_token"));
        document.put("token_endpoint_auth_methods_supported", List.of("none"));
        document.put("code_challenge_methods_supported", List.of("S256"));
        document.put("client_id_metadata_document_supported", Boolean.TRUE);
        document.put("service_documentation", mcpProperties.getDocumentationUrl());
        return document;
    }
}
