package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthAuthorizationManager;
import co.jinear.core.model.request.oauth.OauthAuthorizeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The RFC 6749 authorization endpoint. A browser lands here and is sent on, so it answers with
 * a redirect rather than a response body. Failures are rendered as a page by
 * {@code OauthAuthorizeApiAdvice}.
 */
@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class OauthAuthorizeRedirectController {

    private final OauthAuthorizationManager oauthAuthorizationManager;

    @GetMapping("/authorize")
    public RedirectView authorize(@ModelAttribute OauthAuthorizeRequest oauthAuthorizeRequest) {
        RedirectView redirectView = new RedirectView(oauthAuthorizationManager.authorize(oauthAuthorizeRequest));
        redirectView.setStatusCode(HttpStatus.FOUND);
        return redirectView;
    }
}
