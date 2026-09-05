package co.jinear.core.service.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class McpDiscoveryService {

    private final McpProperties mcpProperties;
    private final OauthProperties oauthProperties;

    public Map<String, Object> protectedResourceMetadata() {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("resource", mcpProperties.getResourceUrl());
        document.put("authorization_servers", List.of(oauthProperties.getIssuerUrl()));
        document.put("scopes_supported", List.copyOf(OauthScope.allValues()));
        document.put("bearer_methods_supported", List.of("header"));
        document.put("resource_documentation", mcpProperties.getDocumentationUrl());
        return document;
    }

    public String protectedResourceMetadataUrl() {
        return oauthProperties.getIssuerUrl() + "/.well-known/oauth-protected-resource/mcp";
    }
}
