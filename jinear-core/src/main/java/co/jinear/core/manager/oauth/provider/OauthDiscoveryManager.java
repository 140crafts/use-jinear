package co.jinear.core.manager.oauth.provider;

import co.jinear.core.model.response.oauth.OauthProtectedResourceMetadataResponse;
import co.jinear.core.model.response.oauth.OauthServerMetadataResponse;
import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.service.oauth.provider.OauthDiscoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthDiscoveryManager {

    private final OauthDiscoveryService oauthDiscoveryService;
    private final McpDiscoveryService mcpDiscoveryService;

    public OauthProtectedResourceMetadataResponse retrieveProtectedResourceMetadata() {
        return mcpDiscoveryService.protectedResourceMetadata();
    }

    public OauthServerMetadataResponse retrieveAuthorizationServerMetadata() {
        return oauthDiscoveryService.authorizationServerMetadata();
    }
}
