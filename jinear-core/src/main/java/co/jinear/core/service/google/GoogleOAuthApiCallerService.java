package co.jinear.core.service.google;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.converter.google.GenerateUserConsentUrlVoToUrlConverter;
import co.jinear.core.converter.google.GoogleTokenVoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.vo.google.GenerateUserConsentUrlVo;
import co.jinear.core.model.vo.google.GetAuthTokenVo;
import co.jinear.core.model.vo.google.GetRefreshTokenVo;
import co.jinear.core.system.gcloud.auth.GoogleAuthClient;
import co.jinear.core.system.gcloud.auth.model.request.GoogleAuthTokenRequest;
import co.jinear.core.system.gcloud.auth.model.request.GoogleRefreshTokenRequest;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthApiCallerService {

    private final GCloudProperties gCloudProperties;
    private final GoogleAuthClient googleAuthClient;
    private final GenerateUserConsentUrlVoToUrlConverter generateUserConsentUrlVoToUrlConverter;
    private final GoogleTokenVoConverter googleTokenVoConverter;

    public String generateUserConsentUrl(GenerateUserConsentUrlVo generateUserConsentUrlVo) {
        log.info("Generate user consent url has started. generateUserConsentUrlVo: {}", generateUserConsentUrlVo);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    public AuthTokenResponse getToken(GetAuthTokenVo getAuthTokenVo) {
        GoogleAuthTokenRequest googleAuthTokenRequest = googleTokenVoConverter.convert(getAuthTokenVo);
        return googleAuthClient.getToken(googleAuthTokenRequest);
    }

    public AuthTokenResponse getRefreshToken(GetRefreshTokenVo getRefreshTokenVo) {
        GoogleRefreshTokenRequest googleRefreshTokenRequest = googleTokenVoConverter.convert(getRefreshTokenVo);
        return googleAuthClient.refreshToken(googleRefreshTokenRequest);
    }

    public TokenInfoResponse tokenInfo(String idToken) {
        TokenInfoResponse tokenInfoResponse = googleAuthClient.tokenInfo(idToken);
        validateAud(tokenInfoResponse);
        return tokenInfoResponse;
    }

    private void validateAud(TokenInfoResponse tokenInfoResponse) {
        if (!gCloudProperties.getOauthClientId().equals(tokenInfoResponse.getAud())) {
            log.error("Aud validation failed. responseAud: {}", tokenInfoResponse.getAud());
            throw new NoAccessException();
        }
    }
}
