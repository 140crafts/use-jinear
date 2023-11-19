package co.jinear.core.converter.google;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.GetAuthTokenVo;
import co.jinear.core.model.vo.google.GetRefreshTokenVo;
import co.jinear.core.system.gcloud.auth.model.request.GoogleAuthTokenRequest;
import co.jinear.core.system.gcloud.auth.model.request.GoogleRefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleTokenVoConverter {

    private final GCloudProperties gCloudProperties;

    public GoogleAuthTokenRequest convert(GetAuthTokenVo getAuthTokenVo) {
        String redirectUrl = getRedirectUrl(getAuthTokenVo.getUserConsentPurposeType());

        GoogleAuthTokenRequest request = new GoogleAuthTokenRequest();
        request.setClientId(gCloudProperties.getOauthClientId());
        request.setClientSecret(gCloudProperties.getOauthClientSecret());
        request.setCode(getAuthTokenVo.getCode());
        request.setRedirectUri(redirectUrl);

        return request;
    }

    public GoogleRefreshTokenRequest convert(GetRefreshTokenVo getRefreshTokenVo) {
        GoogleRefreshTokenRequest request = new GoogleRefreshTokenRequest();

        request.setClientId(gCloudProperties.getOauthClientId());
        request.setClientSecret(gCloudProperties.getOauthClientSecret());
        request.setRefreshToken(getRefreshTokenVo.getRefreshToken());

        return request;
    }

    private String getRedirectUrl(UserConsentPurposeType userConsentPurposeType) {
        return switch (userConsentPurposeType) {
            case LOGIN -> gCloudProperties.getLoginRedirectUrl();
            case ATTACH_ACCOUNT -> gCloudProperties.getAttachAccountRedirectUrl();
        };
    }
}
