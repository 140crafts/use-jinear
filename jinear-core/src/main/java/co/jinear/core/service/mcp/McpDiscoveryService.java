package co.jinear.core.service.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RFC 9728 Protected Resource Metadata for the MCP server: what a client reads to learn
 * which authorization server guards this resource.
 * <p>
 * A plain map rather than a typed response so it serializes exactly as the RFC specifies,
 * and so it stays out of the generated frontend type file. The authorization server
 * describes itself separately, in
 * {@link co.jinear.core.service.oauth.provider.OauthDiscoveryService}.
 */
@Service
@RequiredArgsConstructor
public class McpDiscoveryService {

    private final McpProperties mcpProperties;
    private final OauthProperties oauthProperties;

    /**
     * The {@code resource} value must equal the MCP server URL exactly as a user types
     * it into their client, including the path, or the client refuses the token it
     * later receives.
     */
    public Map<String, Object> protectedResourceMetadata() {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("resource", mcpProperties.getResourceUrl());
        document.put("authorization_servers", List.of(oauthProperties.getIssuerUrl()));
        document.put("scopes_supported", List.copyOf(OauthScope.allValues()));
        document.put("bearer_methods_supported", List.of("header"));
        document.put("resource_documentation", mcpProperties.getDocumentationUrl());
        return document;
    }

    /** Where a 401 from the MCP endpoint points the client, in its WWW-Authenticate header. */
    public String protectedResourceMetadataUrl() {
        return oauthProperties.getIssuerUrl() + "/.well-known/oauth-protected-resource/mcp";
    }
}
