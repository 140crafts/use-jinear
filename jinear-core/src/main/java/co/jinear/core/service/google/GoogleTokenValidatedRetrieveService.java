package co.jinear.core.service.google;

import co.jinear.core.converter.google.AuthTokenResponseToGoogleAuthTokenVoConverter;
import co.jinear.core.converter.google.GoogleTokenToDtoConverter;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.entity.google.GoogleToken;
import co.jinear.core.model.vo.google.GetRefreshTokenVo;
import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenValidatedRetrieveService {

    private final GoogleOAuthApiCallerService googleOAuthApiCallerService;
    private final GoogleTokenOperationService googleTokenOperationService;
    private final GoogleTokenRetrieveService googleTokenRetrieveService;
    private final AuthTokenResponseToGoogleAuthTokenVoConverter authTokenResponseToGoogleAuthTokenVoConverter;
    private final GoogleTokenToDtoConverter googleTokenToDtoConverter;

    public GoogleTokenDto retrieveValidatedToken(String googleUserInfoId) {
        log.info("Retrieve validated token has started. googleUserInfoId: {}", googleUserInfoId);
        GoogleToken googleToken = googleTokenRetrieveService.retrieveEntity(googleUserInfoId);
        return Optional.of(googleToken)
                .filter(this::isStillValid)
                .map(googleTokenToDtoConverter::convert)
                .orElseGet(() -> refreshAndUpdateToken(googleUserInfoId, googleToken));
    }

    private boolean isStillValid(GoogleToken googleToken) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiresAt = googleToken.getExpiresAt();
        log.info("Checking google token is still valid. now: {}, expiresAt: {}",now,expiresAt);
        return now.isBefore(expiresAt);
    }

    private GoogleTokenDto refreshAndUpdateToken(String googleUserInfoId, GoogleToken googleToken) {
        GetRefreshTokenVo getRefreshTokenVo = getGetRefreshTokenVo(googleToken);
        AuthTokenResponse authTokenResponse = googleOAuthApiCallerService.getRefreshToken(getRefreshTokenVo);
        InitializeOrUpdateGoogleAuthTokenVo googleAuthTokenVo = authTokenResponseToGoogleAuthTokenVoConverter.convert(authTokenResponse, googleUserInfoId);
        return googleTokenOperationService.initializeOrUpdateGoogleToken(googleAuthTokenVo);
    }

    private GetRefreshTokenVo getGetRefreshTokenVo(GoogleToken googleToken) {
        GetRefreshTokenVo getRefreshTokenVo = new GetRefreshTokenVo();
        getRefreshTokenVo.setRefreshToken(googleToken.getRefreshToken());
        return getRefreshTokenVo;
    }
}
