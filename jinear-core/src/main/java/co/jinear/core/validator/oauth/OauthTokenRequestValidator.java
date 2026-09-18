package co.jinear.core.validator.oauth;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.oauth.OauthAuthorizationCodeDto;
import co.jinear.core.model.request.oauth.OauthTokenRequest;
import co.jinear.core.service.oauth.provider.PkceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Checks a token request against the authorization code it presents: the client, the redirect
 * URI, the PKCE verifier and the resource indicator must all match what was authorized.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OauthTokenRequestValidator {

    private static final String INVALID_GRANT = "oauth.error.invalid-grant";

    private final PkceValidator pkceValidator;
    private final OauthResourceIndicatorValidator oauthResourceIndicatorValidator;

    public void validateAuthorizationCodeGrant(OauthTokenRequest request, OauthAuthorizationCodeDto code) {
        String clientId = request.getClientId();
        if (Objects.nonNull(clientId) && !clientId.equals(code.getClientId())) {
            log.warn("[OAUTH] client_id at the token endpoint does not match the code. codeClient: {}", code.getClientId());
            throw new BusinessException(INVALID_GRANT);
        }
        if (!Objects.equals(code.getRedirectUri(), request.getRedirectUri())) {
            log.warn("[OAUTH] redirect_uri at the token endpoint does not match the authorization request.");
            throw new BusinessException(INVALID_GRANT);
        }
        if (!pkceValidator.verify(request.getCodeVerifier(), code.getCodeChallenge())) {
            log.warn("[OAUTH] PKCE verification failed. clientId: {}", code.getClientId());
            throw new BusinessException(INVALID_GRANT);
        }
        oauthResourceIndicatorValidator.validateResourceMatches(request.getResource(), INVALID_GRANT);
    }

    public void validateRefreshTokenGrant(OauthTokenRequest request) {
        oauthResourceIndicatorValidator.validateResourceMatches(request.getResource(), INVALID_GRANT);
    }
}
