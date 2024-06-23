package co.jinear.core.manager.oauth;

import co.jinear.core.converter.google.GoogleScopeConverter;
import co.jinear.core.manager.auth.AuthCookieManager;
import co.jinear.core.model.dto.google.GoogleHandleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.model.response.auth.AuthResponse;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;
import co.jinear.core.model.vo.calendar.InitializeCalendarVo;
import co.jinear.core.model.vo.feed.InitializeFeedVo;
import co.jinear.core.model.vo.google.AttachAccountStateParameters;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.auth.AuthenticationStrategy;
import co.jinear.core.service.auth.AuthenticationStrategyFactory;
import co.jinear.core.service.calendar.CalendarOperationService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
import co.jinear.core.service.feed.FeedOperationService;
import co.jinear.core.service.feed.FeedRetrieveService;
import co.jinear.core.service.google.GoogleCallbackHandlerService;
import co.jinear.core.service.integration.IntegrationHandleService;
import co.jinear.core.service.passive.PassiveService;
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
    private final WorkspaceValidator workspaceValidator;
    private final FeedOperationService feedOperationService;
    private final FeedRetrieveService feedRetrieveService;
    private final CalendarRetrieveService calendarRetrieveService;
    private final CalendarOperationService calendarOperationService;

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

    public void attachMail(String code, String scopes, String state) {
        String currentAccountId = sessionInfoService.currentAccountId();
        AttachAccountStateParameters parameters = gson.fromJson(state, AttachAccountStateParameters.class);
        String workspaceId = parameters.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Attach mail has started. currentAccountId: {}, parameters: {}", currentAccountId, parameters);
        validateMailScopes(scopes);
        GoogleHandleTokenDto googleHandleTokenDto = googleCallbackHandlerService.handleToken(code, scopes, UserConsentPurposeType.ATTACH_MAIL);
        assignExistingTokenDeletionOwnership(googleHandleTokenDto, currentAccountId);
        String integrationInfoId = initializeIntegration(googleHandleTokenDto, IntegrationScopeType.EMAIL, currentAccountId);
        initializeFeed(currentAccountId, workspaceId, googleHandleTokenDto, integrationInfoId);
    }

    public void attachCalendar(String code, String scopes, String state) {
        String currentAccountId = sessionInfoService.currentAccountId();
        AttachAccountStateParameters parameters = gson.fromJson(state, AttachAccountStateParameters.class);
        String workspaceId = parameters.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Attach calendar has started. currentAccountId: {}", currentAccountId);
        validateCalendarScopes(scopes);
        GoogleHandleTokenDto googleHandleTokenDto = googleCallbackHandlerService.handleToken(code, scopes, UserConsentPurposeType.ATTACH_CALENDAR);
        assignExistingTokenDeletionOwnership(googleHandleTokenDto, currentAccountId);
        String integrationInfoId = initializeIntegration(googleHandleTokenDto, IntegrationScopeType.CALENDAR, currentAccountId);
        initializeCalendar(currentAccountId, workspaceId, googleHandleTokenDto, integrationInfoId);
    }

    private void initializeCalendar(String currentAccountId, String workspaceId, GoogleHandleTokenDto googleHandleTokenDto, String integrationInfoId) {
        Boolean calendarExist = calendarRetrieveService.checkCalendarExist(workspaceId, currentAccountId, integrationInfoId);
        if (Boolean.FALSE.equals(calendarExist)) {
            GoogleUserInfoDto googleUserInfoDto = googleHandleTokenDto.getGoogleUserInfoDto();
            InitializeCalendarVo initializeCalendarVo = new InitializeCalendarVo();
            initializeCalendarVo.setWorkspaceId(workspaceId);
            initializeCalendarVo.setInitializedBy(currentAccountId);
            initializeCalendarVo.setIntegrationInfoId(integrationInfoId);
            initializeCalendarVo.setName(googleUserInfoDto.getEmail());
            calendarOperationService.initializeCalendar(initializeCalendarVo);
        }
    }

    private void initializeFeed(String currentAccountId, String workspaceId, GoogleHandleTokenDto googleHandleTokenDto, String integrationInfoId) {
        Boolean feedExists = feedRetrieveService.checkFeedExist(workspaceId, currentAccountId, integrationInfoId);
        if (Boolean.FALSE.equals(feedExists)) {
            GoogleUserInfoDto googleUserInfoDto = googleHandleTokenDto.getGoogleUserInfoDto();
            InitializeFeedVo initializeFeedVo = new InitializeFeedVo();
            initializeFeedVo.setWorkspaceId(workspaceId);
            initializeFeedVo.setInitializedBy(currentAccountId);
            initializeFeedVo.setIntegrationInfoId(integrationInfoId);
            initializeFeedVo.setName(googleUserInfoDto.getEmail());
            feedOperationService.initializeFeed(initializeFeedVo);
        }
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
                log.error("Validate mail scopes failed. Scope does not exists. scp: {}", scp);
//                throw new BusinessException("integration.google.mail.insufficient-scopes");
            }
        });
    }

    private void validateCalendarScopes(String scopes) {
        List<GoogleScopeType> returnedScopes = googleScopeConverter.convert(scopes);
        List<GoogleScopeType> calendarScopeTypes = GoogleScopeType.getCalendarScopeTypes();
        calendarScopeTypes.forEach(scp -> {
            if (!returnedScopes.contains(scp)) {
                log.error("Validate calendar scopes failed. Scope does not exists. scp: {}", scp);
//                throw new BusinessException("integration.google.mail.insufficient-scopes");
            }
        });
    }

    private AuthResponse mapResponse(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }
}
