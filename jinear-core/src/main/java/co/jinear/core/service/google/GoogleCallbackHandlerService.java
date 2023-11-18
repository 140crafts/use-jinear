package co.jinear.core.service.google;

import co.jinear.core.converter.google.AuthTokenResponseToGoogleAuthTokenVoConverter;
import co.jinear.core.converter.google.TokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter;
import co.jinear.core.model.dto.google.GoogleHandleLoginResponseDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.GetAuthTokenVo;
import co.jinear.core.model.vo.google.InitializeGoogleTokenScopeVo;
import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import co.jinear.core.model.vo.google.InitializeOrUpdateTokenInfoVo;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @Transactional
    public GoogleHandleLoginResponseDto handleLogin(String code, String scopes) {
        GetAuthTokenVo getAuthTokenVo = mapGetAuthTokenVo(code);
        AuthTokenResponse authTokenResponse = googleOAuthApiCallerService.getToken(getAuthTokenVo);
        TokenInfoResponse tokenInfoResponse = googleOAuthApiCallerService.tokenInfo(authTokenResponse.getIdToken());

        InitializeOrUpdateTokenInfoVo initializeOrUpdateTokenInfoVo = tokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter.convert(tokenInfoResponse);
        GoogleUserInfoDto googleUserInfoDto = googleUserInfoOperationService.initializeOrUpdateGoogleUserInfo(initializeOrUpdateTokenInfoVo);

        InitializeOrUpdateGoogleAuthTokenVo initializeOrUpdateGoogleAuthTokenVo = authTokenResponseToGoogleAuthTokenVoConverter.convert(authTokenResponse, googleUserInfoDto.getGoogleUserInfoId());
        GoogleTokenDto googleTokenDto = googleTokenOperationService.initializeOrUpdateGoogleToken(initializeOrUpdateGoogleAuthTokenVo);

        String passiveIdForScopeDeletion = null;
        if (Objects.nonNull(googleTokenDto.getScopes()) && !googleTokenDto.getScopes().isEmpty()) {
            passiveIdForScopeDeletion = passiveService.createUserActionPassive();
            googleTokenScopeOperationService.removeAllWithGoogleTokenId(googleTokenDto.getGoogleTokenId(), passiveIdForScopeDeletion);
        }

        List<InitializeGoogleTokenScopeVo> initializeGoogleTokenScopeVos = convertToInitializeGoogleTokenScopeVo(googleTokenDto.getGoogleTokenId(), scopes);
        googleTokenScopeOperationService.initializeAll(initializeGoogleTokenScopeVos);

        return new GoogleHandleLoginResponseDto(googleUserInfoDto, passiveIdForScopeDeletion);
    }

    private GetAuthTokenVo mapGetAuthTokenVo(String code) {
        GetAuthTokenVo getAuthTokenVo = new GetAuthTokenVo();
        getAuthTokenVo.setCode(code);
        getAuthTokenVo.setUserConsentPurposeType(UserConsentPurposeType.LOGIN);
        return getAuthTokenVo;
    }

    private List<InitializeGoogleTokenScopeVo> convertToInitializeGoogleTokenScopeVo(String googleTokenId, String scopes) {
        return Arrays.stream(scopes.split(NormalizeHelper.SPACE_STRING))
                .map(GoogleScopeType::fromString)
                .map(googleScopeType -> new InitializeGoogleTokenScopeVo(googleTokenId, googleScopeType))
                .toList();
    }
}
