package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.response.oauth.OauthTokenResponse;
import co.jinear.core.system.oauth.OauthTokenHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Mints the access token, and the refresh token when offline access was granted, then shapes
 * the RFC 6749 token response.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OauthTokenIssueService {

    private static final String TOKEN_TYPE_BEARER = "Bearer";
    private static final long SECONDS_PER_MINUTE = 60L;

    private final OauthRefreshTokenService oauthRefreshTokenService;
    private final OauthScopeService oauthScopeService;
    private final OauthTokenHelper oauthTokenHelper;
    private final OauthProperties oauthProperties;

    public OauthTokenResponse issue(OauthConnectionDto connection, Set<String> scopes) {
        String refreshToken = scopes.contains(OauthScope.OFFLINE_ACCESS.getValue())
                ? oauthRefreshTokenService.issue(connection.getOauthConnectionId())
                : null;
        return issue(connection, scopes, refreshToken);
    }

    public OauthTokenResponse issue(OauthConnectionDto connection, Set<String> scopes, String refreshToken) {
        Date expiresAt = DateHelper.addMinutes(DateHelper.now(), oauthProperties.getAccessTokenValidityMinutes());
        String accessToken = oauthTokenHelper.generateAccessToken(
                connection.getAccountId(),
                connection.getOauthConnectionId(),
                connection.getClientId(),
                scopes,
                expiresAt);

        OauthTokenResponse response = new OauthTokenResponse();
        response.setAccessToken(accessToken);
        response.setTokenType(TOKEN_TYPE_BEARER);
        response.setExpiresIn(oauthProperties.getAccessTokenValidityMinutes() * SECONDS_PER_MINUTE);
        response.setScope(oauthScopeService.format(scopes));
        if (Objects.nonNull(refreshToken)) {
            response.setRefreshToken(refreshToken);
        }
        return response;
    }
}
