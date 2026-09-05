package co.jinear.core.controller.oauth.provider;

import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.service.oauth.provider.OauthDiscoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthDiscoveryController {

    private final OauthDiscoveryService oauthDiscoveryService;
    private final McpDiscoveryService mcpDiscoveryService;

    @GetMapping(value = {
            "/.well-known/oauth-protected-resource",
            "/.well-known/oauth-protected-resource/mcp"
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> protectedResourceMetadata() {
        return cached(mcpDiscoveryService.protectedResourceMetadata());
    }

    @GetMapping(value = {
            "/.well-known/oauth-authorization-server",
            "/.well-known/openid-configuration"
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> authorizationServerMetadata() {
        return cached(oauthDiscoveryService.authorizationServerMetadata());
    }

    private ResponseEntity<Map<String, Object>> cached(Map<String, Object> document) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(document);
    }
}
