package co.jinear.core.system.gcloud.auth;

import co.jinear.core.system.gcloud.auth.model.request.GoogleAuthTokenRequest;
import co.jinear.core.system.gcloud.auth.model.request.GoogleRefreshTokenRequest;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;

public interface GoogleAuthClient {

    AuthTokenResponse getToken(GoogleAuthTokenRequest request);

    AuthTokenResponse refreshToken(GoogleRefreshTokenRequest request);

    TokenInfoResponse tokenInfo(String idToken);
}
