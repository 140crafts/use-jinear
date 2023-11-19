package co.jinear.core.manager.oauth;

import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import co.jinear.core.service.google.GoogleRedirectInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthRedirectInfoManager {

    private final GoogleRedirectInfoService googleRedirectInfoService;

    public AuthRedirectInfoResponse retrieveLoginRedirectUrl() {
        log.info("Retrieve login redirect url has started.");
        String redirectUrl = googleRedirectInfoService.retrieveLoginUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachMailUrl() {
        log.info("Retrieve mail attach redirect url has started.");
        String redirectUrl = googleRedirectInfoService.retrieveAttachMailUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachCalendarUrl() {
        log.info("Retrieve calendar attach redirect url has started.");
        String redirectUrl = googleRedirectInfoService.retrieveAttachCalendarUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }
}
