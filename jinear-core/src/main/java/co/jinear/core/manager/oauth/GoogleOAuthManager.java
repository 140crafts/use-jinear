package co.jinear.core.manager.oauth;

import co.jinear.core.manager.auth.AuthCookieManager;
import co.jinear.core.model.dto.google.GoogleHandleLoginResponseDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import co.jinear.core.model.response.auth.AuthResponse;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.auth.AuthenticationStrategy;
import co.jinear.core.service.auth.AuthenticationStrategyFactory;
import co.jinear.core.service.google.GoogleCallbackHandlerService;
import co.jinear.core.service.integration.IntegrationHandleService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static co.jinear.core.model.enumtype.auth.ProviderType.OAUTH_MAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthManager {

    private final AuthenticationStrategyFactory authenticationStrategyFactory;
    private final PassiveService passiveService;
    private final SessionInfoService sessionInfoService;
    private final AuthCookieManager authCookieManager;
    private final JwtHelper jwtHelper;
    private final GoogleCallbackHandlerService googleCallbackHandlerService;
    private final IntegrationHandleService integrationHandleService;

    public AuthRedirectInfoResponse retrieveLoginRedirectUrl() {
        log.info("Retrieve login redirect url has started.");
        String redirectUrl = googleCallbackHandlerService.retrieveLoginUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachMailUrl() {
        log.info("Retrieve mail attach redirect url has started.");
        String redirectUrl = googleCallbackHandlerService.retrieveAttachMailUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachCalendarUrl() {
        log.info("Retrieve calendar attach redirect url has started.");
        String redirectUrl = googleCallbackHandlerService.retrieveAttachCalendarUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthResponse login(String code, String scopes, HttpServletResponse response) {
        log.info("Login with google has started.");
        GoogleHandleLoginResponseDto googleHandleLoginResponseDto = googleCallbackHandlerService.handleLogin(code, scopes);
        AuthResponseVo authResponseVo = authenticateWithGoogleUserInfo(googleHandleLoginResponseDto);
        assignExistingTokenDeletionOwnership(googleHandleLoginResponseDto, authResponseVo);
        String token = initializeSessionInfoAndGenerateJwtToken(authResponseVo);
        initializeIntegration(googleHandleLoginResponseDto, authResponseVo);
        authCookieManager.addAuthCookie(token, response);
        return mapResponse(token);
    }

    public BaseResponse attachAccount(String code, String scopes) {

        return new BaseResponse();

    }

    private void initializeIntegration(GoogleHandleLoginResponseDto googleHandleLoginResponseDto, AuthResponseVo authResponseVo) {
        GoogleUserInfoDto googleUserInfoDto = googleHandleLoginResponseDto.getGoogleUserInfoDto();
        integrationHandleService.initializeOrUpdateInfo(authResponseVo.getAccountId(), IntegrationProvider.GOOGLE, IntegrationScopeType.LOGIN, googleUserInfoDto.getGoogleUserInfoId());
    }

    private void assignExistingTokenDeletionOwnership(GoogleHandleLoginResponseDto googleHandleLoginResponseDto, AuthResponseVo authResponseVo) {
        final String passiveIdForScopeDeletion = googleHandleLoginResponseDto.getPassiveIdForScopeDeletion();
        if (Objects.nonNull(passiveIdForScopeDeletion)) {
            passiveService.assignOwnership(passiveIdForScopeDeletion, authResponseVo.getAccountId());
        }
    }

    private AuthResponseVo authenticateWithGoogleUserInfo(GoogleHandleLoginResponseDto googleHandleLoginResponseDto) {
        GoogleUserInfoDto googleUserInfoDto = googleHandleLoginResponseDto.getGoogleUserInfoDto();
        String email = googleUserInfoDto.getEmail();
        AuthenticationStrategy authenticationStrategy = authenticationStrategyFactory.getStrategy(ProviderType.OAUTH_MAIL);
        AuthVo authVo = new AuthVo();
        authVo.setEmail(email);
        return authenticationStrategy.auth(authVo);
    }


    private String initializeSessionInfoAndGenerateJwtToken(AuthResponseVo authResponseVo) {
        String sessionInfoId = initializeSessionInfo(authResponseVo);
        return jwtHelper.generateToken(authResponseVo, sessionInfoId);
    }

    private String initializeSessionInfo(AuthResponseVo auth) {
        return sessionInfoService.initialize(OAUTH_MAIL, auth.getAccountId());
    }

    private AuthResponse mapResponse(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }
}
