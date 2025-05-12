package co.jinear.core.service.google;

import co.jinear.core.converter.google.AuthTokenResponseToGoogleAuthTokenVoConverter;
import co.jinear.core.converter.google.GoogleScopeConverter;
import co.jinear.core.converter.google.TokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter;
import co.jinear.core.model.dto.google.GoogleHandleTokenDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.GetAuthTokenVo;
import co.jinear.core.model.vo.google.InitializeGoogleTokenScopeVo;
import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import co.jinear.core.model.vo.google.InitializeOrUpdateTokenInfoVo;
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
    private final GoogleScopeConverter googleScopeConverter;

    @Transactional
    public GoogleHandleTokenDto handleToken(String code, String scopes, UserConsentPurposeType userConsentPurposeType) {
        GetAuthTokenVo getAuthTokenVo = mapGetAuthTokenVo(code, userConsentPurposeType);
        AuthTokenResponse authTokenResponse = googleOAuthApiCallerService.getToken(getAuthTokenVo);
        TokenInfoResponse tokenInfoResponse = googleOAuthApiCallerService.tokenInfo(authTokenResponse.getIdToken());

        InitializeOrUpdateTokenInfoVo initializeOrUpdateTokenInfoVo = tokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter.convert(tokenInfoResponse);
        GoogleUserInfoDto googleUserInfoDto = googleUserInfoOperationService.initializeOrUpdateGoogleUserInfo(initializeOrUpdateTokenInfoVo);

        InitializeOrUpdateGoogleAuthTokenVo initializeOrUpdateGoogleAuthTokenVo = authTokenResponseToGoogleAuthTokenVoConverter.convert(authTokenResponse, googleUserInfoDto.getGoogleUserInfoId());
        GoogleTokenDto googleTokenDto = googleTokenOperationService.initializeOrUpdateGoogleToken(initializeOrUpdateGoogleAuthTokenVo);

        String passiveIdForScopeDeletion = checkAnDeleteExistingScopes(googleTokenDto);
        List<InitializeGoogleTokenScopeVo> initializeGoogleTokenScopeVos = googleScopeConverter.convertToInitializeGoogleTokenScopeVo(googleTokenDto.getGoogleTokenId(), scopes);
        googleTokenScopeOperationService.initializeAll(initializeGoogleTokenScopeVos);
        return new GoogleHandleTokenDto(googleUserInfoDto, passiveIdForScopeDeletion);
    }

    private String checkAnDeleteExistingScopes(GoogleTokenDto googleTokenDto) {
        String passiveIdForScopeDeletion = null;
        if (Objects.nonNull(googleTokenDto.getScopes()) && !googleTokenDto.getScopes().isEmpty()) {
            passiveIdForScopeDeletion = passiveService.createUserActionPassive();
            googleTokenScopeOperationService.removeAllWithGoogleTokenId(googleTokenDto.getGoogleTokenId(), passiveIdForScopeDeletion);
        }
        return passiveIdForScopeDeletion;
    }

    private GetAuthTokenVo mapGetAuthTokenVo(String code, UserConsentPurposeType userConsentPurposeType) {
        GetAuthTokenVo getAuthTokenVo = new GetAuthTokenVo();
        getAuthTokenVo.setCode(code);
        getAuthTokenVo.setUserConsentPurposeType(userConsentPurposeType);
        return getAuthTokenVo;
    }
}
