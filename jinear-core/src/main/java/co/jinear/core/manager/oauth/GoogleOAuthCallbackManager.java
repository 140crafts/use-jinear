package co.jinear.core.manager.oauth;

import co.jinear.core.converter.google.GoogleScopeConverter;
import co.jinear.core.converter.team.TeamInitializeVoFromCallbackConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.auth.AuthCookieManager;
import co.jinear.core.model.dto.google.GoogleHandleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.auth.AuthResponse;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.model.vo.google.AttachAccountStateParameters;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.auth.AuthenticationStrategy;
import co.jinear.core.service.auth.AuthenticationStrategyFactory;
import co.jinear.core.service.google.GoogleCallbackHandlerService;
import co.jinear.core.service.integration.IntegrationHandleService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.system.JwtHelper;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static co.jinear.core.model.enumtype.auth.ProviderType.OAUTH_MAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthCallbackManager {

    private final AuthenticationStrategyFactory authenticationStrategyFactory;
    private final PassiveService passiveService;
    private final SessionInfoService sessionInfoService;
    private final AuthCookieManager authCookieManager;
    private final JwtHelper jwtHelper;
    private final GoogleCallbackHandlerService googleCallbackHandlerService;
    private final IntegrationHandleService integrationHandleService;
    private final GoogleScopeConverter googleScopeConverter;
    private final Gson gson;
    private final TeamInitializeService teamInitializeService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamInitializeVoFromCallbackConverter teamInitializeVoFromCallbackConverter;

    public AuthResponse login(String code, String scopes, HttpServletResponse response) {
        log.info("Login with google has started.");
        GoogleHandleTokenDto googleHandleTokenDto = googleCallbackHandlerService.handleToken(code, scopes, UserConsentPurposeType.LOGIN);
        AuthResponseVo authResponseVo = authenticateWithGoogleUserInfo(googleHandleTokenDto);
        assignExistingTokenDeletionOwnership(googleHandleTokenDto, authResponseVo.getAccountId());
        String token = initializeSessionInfoAndGenerateJwtToken(authResponseVo);
        initializeIntegration(googleHandleTokenDto, IntegrationScopeType.LOGIN, authResponseVo.getAccountId());
        authCookieManager.addAuthCookie(token, response);
        return mapResponse(token);
    }

    public BaseResponse attachMail(String code, String scopes, String state) {
        String currentAccountId = sessionInfoService.currentAccountId();
        LocaleType localeType = sessionInfoService.currentAccountLocale().orElse(LocaleType.EN);
        AttachAccountStateParameters parameters = gson.fromJson(state, AttachAccountStateParameters.class);
        String workspaceId = parameters.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Attach mail has started. currentAccountId: {}, parameters: {}", currentAccountId, parameters);
        validateMailScopes(scopes);
        GoogleHandleTokenDto googleHandleTokenDto = googleCallbackHandlerService.handleToken(code, scopes, UserConsentPurposeType.ATTACH_MAIL);
        assignExistingTokenDeletionOwnership(googleHandleTokenDto, currentAccountId);
        String integrationInfoId = initializeIntegration(googleHandleTokenDto, IntegrationScopeType.EMAIL, currentAccountId);
        initializeTeam(currentAccountId, localeType, workspaceId, googleHandleTokenDto, integrationInfoId);
        return new BaseResponse();
    }

    private void initializeTeam(String currentAccountId, LocaleType localeType, String workspaceId, GoogleHandleTokenDto googleHandleTokenDto, String integrationInfoId) {
        TeamInitializeVo teamInitializeVo = teamInitializeVoFromCallbackConverter.mapTeamInitializeVo(currentAccountId, localeType, workspaceId, googleHandleTokenDto, integrationInfoId);
        teamInitializeService.initializeTeam(teamInitializeVo);
    }


    public BaseResponse attachCalendar(String code, String scopes) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Attach calendar has started. currentAccountId: {}", currentAccountId);
        validateCalendarScopes(scopes);
        GoogleHandleTokenDto googleHandleTokenDto = googleCallbackHandlerService.handleToken(code, scopes, UserConsentPurposeType.ATTACH_CALENDAR);
        assignExistingTokenDeletionOwnership(googleHandleTokenDto, currentAccountId);
        initializeIntegration(googleHandleTokenDto, IntegrationScopeType.CALENDAR, currentAccountId);
        return new BaseResponse();
    }

    private String initializeIntegration(GoogleHandleTokenDto googleHandleTokenDto, IntegrationScopeType scope, String accountId) {
        GoogleUserInfoDto googleUserInfoDto = googleHandleTokenDto.getGoogleUserInfoDto();
        return integrationHandleService.initializeOrUpdateInfo(accountId, IntegrationProvider.GOOGLE, scope, googleUserInfoDto.getGoogleUserInfoId());
    }

    private void assignExistingTokenDeletionOwnership(GoogleHandleTokenDto googleHandleTokenDto, String accountId) {
        final String passiveIdForScopeDeletion = googleHandleTokenDto.getPassiveIdForScopeDeletion();
        if (Objects.nonNull(passiveIdForScopeDeletion)) {
            passiveService.assignOwnership(passiveIdForScopeDeletion, accountId);
        }
    }

    private AuthResponseVo authenticateWithGoogleUserInfo(GoogleHandleTokenDto googleHandleTokenDto) {
        GoogleUserInfoDto googleUserInfoDto = googleHandleTokenDto.getGoogleUserInfoDto();
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

    private void validateMailScopes(String scopes) {
        List<GoogleScopeType> returnedScopes = googleScopeConverter.convert(scopes);
        List<GoogleScopeType> mailScopeTypes = GoogleScopeType.getMailScopeTypes();
        mailScopeTypes.forEach(scp -> {
            if (!returnedScopes.contains(scp)) {
                throw new BusinessException("integration.google.mail.insufficient-scopes");
            }
        });
    }

    private void validateCalendarScopes(String scopes) {
        List<GoogleScopeType> returnedScopes = googleScopeConverter.convert(scopes);
        List<GoogleScopeType> calendarScopeTypes = GoogleScopeType.getCalendarScopeTypes();
        returnedScopes.forEach(scp -> {
            if (!calendarScopeTypes.contains(scp)) {
                throw new BusinessException("integration.google.mail.insufficient-scopes");
            }
        });
    }

    private AuthResponse mapResponse(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }
}
