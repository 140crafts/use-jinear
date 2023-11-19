package co.jinear.core.service.google;

import co.jinear.core.converter.google.AuthTokenResponseToGoogleAuthTokenVoConverter;
import co.jinear.core.converter.google.GenerateUserConsentUrlVoToUrlConverter;
import co.jinear.core.converter.google.GoogleScopeConverter;
import co.jinear.core.converter.google.TokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter;
import co.jinear.core.model.dto.google.GoogleHandleLoginResponseDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.*;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCallbackHandlerService {

    private final GoogleOAuthApiCallerService googleOAuthApiCallerService;
    private final GoogleUserInfoOperationService googleUserInfoOperationService;
    private final GoogleTokenOperationService googleTokenOperationService;
    private final GoogleTokenScopeOperationService googleTokenScopeOperationService;
    private final TokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter tokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter;
    private final AuthTokenResponseToGoogleAuthTokenVoConverter authTokenResponseToGoogleAuthTokenVoConverter;
    private final PassiveService passiveService;
    private final GenerateUserConsentUrlVoToUrlConverter generateUserConsentUrlVoToUrlConverter;
    private final GoogleScopeConverter googleScopeConverter;

    public String retrieveLoginUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.LOGIN);
        generateUserConsentUrlVo.setIncludeEmailScopes(Boolean.FALSE);
        generateUserConsentUrlVo.setIncludeCalendarScopes(Boolean.FALSE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    public String retrieveAttachMailUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.ATTACH_MAIL);
        generateUserConsentUrlVo.setIncludeEmailScopes(Boolean.TRUE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    public String retrieveAttachCalendarUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.ATTACH_CALENDAR);
        generateUserConsentUrlVo.setIncludeCalendarScopes(Boolean.TRUE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    @Transactional
    public GoogleHandleLoginResponseDto handleLogin(String code, String scopes) {
        GetAuthTokenVo getAuthTokenVo = mapGetAuthTokenVo(code);
        AuthTokenResponse authTokenResponse = googleOAuthApiCallerService.getToken(getAuthTokenVo);
        TokenInfoResponse tokenInfoResponse = googleOAuthApiCallerService.tokenInfo(authTokenResponse.getIdToken());

        InitializeOrUpdateTokenInfoVo initializeOrUpdateTokenInfoVo = tokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter.convert(tokenInfoResponse);
        GoogleUserInfoDto googleUserInfoDto = googleUserInfoOperationService.initializeOrUpdateGoogleUserInfo(initializeOrUpdateTokenInfoVo);

        InitializeOrUpdateGoogleAuthTokenVo initializeOrUpdateGoogleAuthTokenVo = authTokenResponseToGoogleAuthTokenVoConverter.convert(authTokenResponse, googleUserInfoDto.getGoogleUserInfoId());
        GoogleTokenDto googleTokenDto = googleTokenOperationService.initializeOrUpdateGoogleToken(initializeOrUpdateGoogleAuthTokenVo);

        String passiveIdForScopeDeletion = checkAnDeleteExistingScopes(googleTokenDto);
        List<InitializeGoogleTokenScopeVo> initializeGoogleTokenScopeVos = googleScopeConverter.convertToInitializeGoogleTokenScopeVo(googleTokenDto.getGoogleTokenId(), scopes);
        googleTokenScopeOperationService.initializeAll(initializeGoogleTokenScopeVos);
        return new GoogleHandleLoginResponseDto(googleUserInfoDto, passiveIdForScopeDeletion);
    }

    private String checkAnDeleteExistingScopes(GoogleTokenDto googleTokenDto) {
        String passiveIdForScopeDeletion = null;
        if (Objects.nonNull(googleTokenDto.getScopes()) && !googleTokenDto.getScopes().isEmpty()) {
            passiveIdForScopeDeletion = passiveService.createUserActionPassive();
            googleTokenScopeOperationService.removeAllWithGoogleTokenId(googleTokenDto.getGoogleTokenId(), passiveIdForScopeDeletion);
        }
        return passiveIdForScopeDeletion;
    }

    private GetAuthTokenVo mapGetAuthTokenVo(String code) {
        GetAuthTokenVo getAuthTokenVo = new GetAuthTokenVo();
        getAuthTokenVo.setCode(code);
        getAuthTokenVo.setUserConsentPurposeType(UserConsentPurposeType.LOGIN);
        return getAuthTokenVo;
    }
}
