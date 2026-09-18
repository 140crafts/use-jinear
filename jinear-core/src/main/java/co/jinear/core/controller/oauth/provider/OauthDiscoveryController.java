package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthDiscoveryManager;
import co.jinear.core.model.response.oauth.OauthProtectedResourceMetadataResponse;
import co.jinear.core.model.response.oauth.OauthServerMetadataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RFC 8414 and RFC 9728 discovery documents. Their paths are well known URIs fixed by the
 * specifications, so this controller has no {@code v1} base path.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthDiscoveryController {

    private final OauthDiscoveryManager oauthDiscoveryManager;

    @GetMapping(value = {
            "/.well-known/oauth-protected-resource",
            "/.well-known/oauth-protected-resource/mcp"
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OauthProtectedResourceMetadataResponse protectedResourceMetadata() {
        return oauthDiscoveryManager.retrieveProtectedResourceMetadata();
    }

    @GetMapping(value = {
            "/.well-known/oauth-authorization-server",
            "/.well-known/openid-configuration"
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OauthServerMetadataResponse authorizationServerMetadata() {
        return oauthDiscoveryManager.retrieveAuthorizationServerMetadata();
    }
}
