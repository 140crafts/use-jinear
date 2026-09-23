package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthTokenManager;
import co.jinear.core.model.request.oauth.OauthClientRegistrationRequest;
import co.jinear.core.model.request.oauth.OauthRevokeRequest;
import co.jinear.core.model.request.oauth.OauthTokenRequest;
import co.jinear.core.model.response.oauth.OauthClientRegistrationResponse;
import co.jinear.core.model.response.oauth.OauthTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The OAuth token, registration and revocation endpoints. Their bodies follow RFC 6749, 7591
 * and 7009 rather than the Jinear response envelope; errors are rendered by
 * {@code OauthApiAdvice}.
 */
@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class OauthTokenController {

    private final OauthTokenManager oauthTokenManager;

    @PostMapping(value = "/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OauthTokenResponse token(@ModelAttribute OauthTokenRequest oauthTokenRequest) {
        return oauthTokenManager.token(oauthTokenRequest);
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OauthClientRegistrationResponse register(@RequestBody OauthClientRegistrationRequest oauthClientRegistrationRequest) {
        return oauthTokenManager.register(oauthClientRegistrationRequest);
    }

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void revoke(@ModelAttribute OauthRevokeRequest oauthRevokeRequest) {
        oauthTokenManager.revoke(oauthRevokeRequest);
    }
}
