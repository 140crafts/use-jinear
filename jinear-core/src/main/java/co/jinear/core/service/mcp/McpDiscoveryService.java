package co.jinear.core.service.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.response.oauth.OauthProtectedResourceMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class McpDiscoveryService {

    private final McpProperties mcpProperties;
    private final OauthProperties oauthProperties;

    public OauthProtectedResourceMetadataResponse protectedResourceMetadata() {
        OauthProtectedResourceMetadataResponse metadata = new OauthProtectedResourceMetadataResponse();
        metadata.setResource(mcpProperties.getResourceUrl());
        metadata.setAuthorizationServers(List.of(oauthProperties.getIssuerUrl()));
        metadata.setScopesSupported(List.copyOf(OauthScope.allValues()));
        metadata.setBearerMethodsSupported(List.of("header"));
        metadata.setResourceDocumentation(mcpProperties.getDocumentationUrl());
        return metadata;
    }

    public String protectedResourceMetadataUrl() {
        return oauthProperties.getIssuerUrl() + "/.well-known/oauth-protected-resource/mcp";
    }
}
