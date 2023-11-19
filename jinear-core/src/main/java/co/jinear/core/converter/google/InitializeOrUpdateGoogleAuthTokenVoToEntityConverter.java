package co.jinear.core.converter.google;

import co.jinear.core.model.entity.google.GoogleToken;
import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;


@Slf4j
@Component
public class InitializeOrUpdateGoogleAuthTokenVoToEntityConverter {

    public GoogleToken map(InitializeOrUpdateGoogleAuthTokenVo googleAuthTokenVo, Optional<GoogleToken> optionalGoogleToken) {
        GoogleToken googleToken = optionalGoogleToken.map(entity -> {
            log.info("Google token is present mapping new values to existing entity.");
            return entity;
        }).orElseGet(() -> {
            log.info("Initializing new google token entity.");
            return new GoogleToken();
        });
        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getExpiresIn)
                .map(expiresIn -> ZonedDateTime.now().plusSeconds(expiresIn))
                .ifPresent(googleToken::setExpiresAt);

        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getAccessToken)
                .ifPresent(googleToken::setAccessToken);

        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getRefreshToken)
                .ifPresent(googleToken::setRefreshToken);

        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getIdToken)
                .ifPresent(googleToken::setIdToken);

        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getTokenType)
                .ifPresent(googleToken::setTokenType);

        Optional.of(googleAuthTokenVo)
                .map(InitializeOrUpdateGoogleAuthTokenVo::getGoogleUserInfoId)
                .ifPresent(googleToken::setGoogleUserInfoId);
        return googleToken;
    }
}
