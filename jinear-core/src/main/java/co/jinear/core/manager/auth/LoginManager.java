package co.jinear.core.manager.auth;

import co.jinear.core.converter.auth.AuthVoConverter;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.request.auth.AuthCompleteRequest;
import co.jinear.core.model.request.auth.AuthInitializeRequest;
import co.jinear.core.model.request.auth.LoginWithPasswordRequest;
import co.jinear.core.model.response.auth.AuthInitializeResponse;
import co.jinear.core.model.response.auth.AuthResponse;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountLoginInitializeService;
import co.jinear.core.service.account.AccountUpdateService;
import co.jinear.core.service.auth.AuthenticationStrategy;
import co.jinear.core.service.auth.AuthenticationStrategyFactory;
import co.jinear.core.system.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.auth.ProviderType.OTP_MAIL;
import static co.jinear.core.model.enumtype.auth.ProviderType.PASSWORD_MAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginManager {

    private final AccountLoginInitializeService accountLoginInitializeService;
    private final AuthenticationStrategyFactory authenticationStrategyFactory;
    private final SessionInfoService sessionInfoService;
    private final AuthCookieManager authCookieManager;
    private final JwtHelper jwtHelper;
    private final AuthVoConverter authVoConverter;
    private final AccountUpdateService accountUpdateService;

    public AuthInitializeResponse loginWithEmailOtpInitialize(AuthInitializeRequest authInitializeRequest) {
        log.info("Login with email has started.");
        AuthVo authVo = authVoConverter.map(authInitializeRequest);
        AuthVo initializedAuthVo = accountLoginInitializeService.emailLoginTokenRequest(authVo);
        log.info("Login with email has finished. initializedAuthVo: {}", initializedAuthVo);
        return authVoConverter.map(initializedAuthVo);
    }

    @Transactional
    public AuthResponse emailOtpLoginComplete(AuthCompleteRequest authCompleteRequest, HttpServletResponse response) {
        log.info("Email otp login complete has started. authCompleteRequest: {}", authCompleteRequest);
        ProviderType providerType = authCompleteRequest.getProvider();
        AuthVo authVo = authVoConverter.map(authCompleteRequest);
        AuthResponseVo authResponseVo = retrieveStrategyAndAuth(providerType, authVo);
        String token = initializeSessionInfoAndGenerateJwtToken(OTP_MAIL, authResponseVo);
        AuthResponse authResponse = mapResponse(token);
        authCookieManager.addAuthCookie(token, response);
        accountUpdateService.updateAccountLocale(authResponseVo.getAccountId(), authCompleteRequest.getLocale());
        return authResponse;
    }

    public AuthResponse loginWithPassword(LoginWithPasswordRequest loginWithPasswordRequest, HttpServletResponse response) {
        log.info("Login with password has started. email: {}", loginWithPasswordRequest.getEmail());
        AuthVo authVo = mapRequest(loginWithPasswordRequest);
        AuthResponseVo authResponseVo = retrieveStrategyAndAuth(PASSWORD_MAIL, authVo);
        String token = initializeSessionInfoAndGenerateJwtToken(PASSWORD_MAIL, authResponseVo);
        AuthResponse authResponse = mapResponse(token);
        authCookieManager.addAuthCookie(token, response);
        accountUpdateService.updateAccountLocale(authResponseVo.getAccountId(), loginWithPasswordRequest.getLocale());
        return authResponse;
    }

    private AuthResponseVo retrieveStrategyAndAuth(ProviderType providerType, AuthVo authVo) {
        log.info("Retrieve strategy and auth has started for providerType: {}", providerType);
        AuthenticationStrategy authenticationStrategy = authenticationStrategyFactory.getStrategy(providerType);
        return authenticationStrategy.auth(authVo);
    }

    private String initializeSessionInfoAndGenerateJwtToken(ProviderType providerType, AuthResponseVo authResponseVo) {
        String sessionInfoId = initializeSessionInfo(providerType, authResponseVo);
        return jwtHelper.generateToken(authResponseVo, sessionInfoId);
    }

    private String initializeSessionInfo(ProviderType providerType, AuthResponseVo auth) {
        return sessionInfoService.initialize(providerType, auth.getAccountId());
    }

    private AuthResponse mapResponse(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }

    private AuthVo mapRequest(LoginWithPasswordRequest loginWithPasswordRequest) {
        AuthVo authVo = new AuthVo();
        authVo.setEmail(loginWithPasswordRequest.getEmail());
        authVo.setCode(loginWithPasswordRequest.getPassword());
        authVo.setLocale(loginWithPasswordRequest.getLocale());
        return authVo;
    }
}
