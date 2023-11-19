package co.jinear.core.controller.oauth;

import co.jinear.core.manager.oauth.GoogleOAuthManager;
import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/oauth/google/redirect-info")
public class GoogleOAuthController {

    private final GoogleOAuthManager googleOAuthManager;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveLoginRedirectInfo() {
        return googleOAuthManager.retrieveLoginRedirectUrl();
    }

    @GetMapping("/attach-mail")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveAccountAttachRedirectUrl() {
        return googleOAuthManager.retrieveAttachMailUrl();
    }

    @GetMapping("/attach-calendar")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveCalendarAttachRedirectUrl() {
        return googleOAuthManager.retrieveAttachCalendarUrl();
    }
}
