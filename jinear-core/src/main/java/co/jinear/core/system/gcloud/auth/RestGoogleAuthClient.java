package co.jinear.core.system.gcloud.auth;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.system.gcloud.auth.model.request.GoogleAuthTokenRequest;
import co.jinear.core.system.gcloud.auth.model.request.GoogleRefreshTokenRequest;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.google-auth-client.enabled", havingValue = "false", matchIfMissing = true)
public class RestGoogleAuthClient implements GoogleAuthClient {

    private static final String TOKEN = "/token";
    private static final String TOKEN_INFO = "/tokeninfo?id_token=%s";

    private final RestTemplate googleAuthRestTemplate;

    @Override
    public AuthTokenResponse getToken(GoogleAuthTokenRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", request.getCode());
            map.add("redirect_uri", request.getRedirectUri());
            map.add("grant_type", request.getGrantType().getValue());
            map.add("client_id", request.getClientId());
            map.add("client_secret", request.getClientSecret());
            map.add("access_type", request.getAccessType().getValue());

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<AuthTokenResponse> response = googleAuthRestTemplate.postForEntity(TOKEN, requestEntity, AuthTokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Get token call has failed.", e);
            throw new BusinessException();
        }
    }

    @Override
    public AuthTokenResponse refreshToken(GoogleRefreshTokenRequest request) {
        return googleAuthRestTemplate.postForObject(TOKEN, request, AuthTokenResponse.class);
    }

    @Override
    public TokenInfoResponse tokenInfo(String idToken) {
        return googleAuthRestTemplate.getForObject(TOKEN_INFO.formatted(idToken), TokenInfoResponse.class);
    }
}
