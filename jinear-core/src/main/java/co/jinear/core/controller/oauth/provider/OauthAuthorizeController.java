package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthAuthorizationManager;
import co.jinear.core.model.request.oauth.OauthConsentRequest;
import co.jinear.core.model.response.oauth.OauthConsentInfoResponse;
import co.jinear.core.model.response.oauth.OauthConsentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The consent screen's own API. These are ordinary Jinear endpoints called by the frontend,
 * so they use the usual response envelope, unlike the redirect endpoint next door.
 */
@Slf4j
@RestController
@RequestMapping(value = "v1/oauth/authorize")
@RequiredArgsConstructor
public class OauthAuthorizeController {

    private final OauthAuthorizationManager oauthAuthorizationManager;

    @GetMapping("/info/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public OauthConsentInfoResponse retrieveConsentInfo(@PathVariable String requestId) {
        return oauthAuthorizationManager.retrieveConsentInfo(requestId);
    }

    @PostMapping("/consent")
    @ResponseStatus(HttpStatus.OK)
    public OauthConsentResponse submitConsent(@Valid @RequestBody OauthConsentRequest oauthConsentRequest) {
        return oauthAuthorizationManager.submitConsent(oauthConsentRequest);
    }
}
