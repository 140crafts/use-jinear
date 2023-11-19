package co.jinear.core.controller.oauth;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.manager.oauth.GoogleOAuthManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/oauth/google")
public class GoogleOAuthController {

    private final FeProperties feProperties;
    private final GoogleOAuthManager googleOAuthManager;

    @GetMapping("/login-redirect-info")
    @ResponseStatus(HttpStatus.OK)
    public AuthRedirectInfoResponse retrieveLoginRedirectInfo() {
        return googleOAuthManager.retrieveLoginRedirectUrl();
    }

    @GetMapping("/callback/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestParam String code, @RequestParam String scope, HttpServletResponse response) throws IOException {
        googleOAuthManager.login(code, scope, response);
        response.sendRedirect(feProperties.getHomeUrl());
    }

    @GetMapping("/callback/attach-account")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse attachAccount(@RequestParam String code, @RequestParam String scope, HttpEntity<String> httpEntity) {
//        GetAuthTokenVo getAuthTokenVo = new GetAuthTokenVo();
//        getAuthTokenVo.setCode(code);
//        getAuthTokenVo.setUserConsentPurposeType(UserConsentPurposeType.ATTACH_ACCOUNT);
//
//        AuthTokenResponse authTokenResponse = googleOAuthApiCallerService.getToken(getAuthTokenVo);
//        log.info("authTokenResponse: {}", authTokenResponse);
//
//        TokenInfoResponse tokenInfoResponse = googleOAuthApiCallerService.tokenInfo(authTokenResponse.getIdToken());
//        log.info("tokenInfoResponse: {}", tokenInfoResponse);
        return new BaseResponse();
    }

}
