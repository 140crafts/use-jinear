package co.jinear.core.controller.oauth;

import co.jinear.core.manager.oauth.GoogleOAuthRedirectInfoManager;
import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/oauth/google/redirect-info")
public class GoogleOAuthRedirectInfoController {

    private final GoogleOAuthRedirectInfoManager googleOAuthRedirectInfoManager;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveLoginRedirectInfo() {
        return googleOAuthRedirectInfoManager.retrieveLoginRedirectUrl();
    }

    @GetMapping("/attach-mail")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveAccountAttachRedirectUrl(@RequestParam String workspaceId) {
        return googleOAuthRedirectInfoManager.retrieveAttachMailUrl(workspaceId);
    }

    @GetMapping("/attach-calendar")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveCalendarAttachRedirectUrl() {
        return googleOAuthRedirectInfoManager.retrieveAttachCalendarUrl();
    }
}
