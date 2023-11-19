package co.jinear.core.system.gcloud.auth;

import co.jinear.core.system.gcloud.auth.model.request.GoogleAuthTokenRequest;
import co.jinear.core.system.gcloud.auth.model.request.GoogleRefreshTokenRequest;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.google-auth-client.enabled", havingValue = "true")
public class MockGoogleAuthClient implements GoogleAuthClient {

    @Override
    public AuthTokenResponse getToken(GoogleAuthTokenRequest request) {
        log.info("[MOCK] Get token call has started.");
        return new AuthTokenResponse();
    }

    @Override
    public AuthTokenResponse refreshToken(GoogleRefreshTokenRequest request) {
        log.info("[MOCK] Refresh token call has started.");
        return new AuthTokenResponse();
    }

    @Override
    public TokenInfoResponse tokenInfo(String idToken) {
        log.info("[MOCK] Token info call has started.");
        return null;
    }
}
