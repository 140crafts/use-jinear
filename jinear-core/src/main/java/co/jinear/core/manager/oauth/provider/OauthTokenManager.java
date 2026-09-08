package co.jinear.core.manager.oauth.provider;

import co.jinear.core.converter.oauth.OauthClientMetadataVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.oauth.OauthAuthorizationCodeDto;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.model.dto.oauth.OauthRefreshTokenDto;
import co.jinear.core.model.request.oauth.OauthClientRegistrationRequest;
import co.jinear.core.model.request.oauth.OauthRevokeRequest;
import co.jinear.core.model.request.oauth.OauthTokenRequest;
import co.jinear.core.model.response.oauth.OauthClientRegistrationResponse;
import co.jinear.core.model.response.oauth.OauthTokenResponse;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.oauth.provider.OauthAuthorizationCodeService;
import co.jinear.core.service.oauth.provider.OauthClientService;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.service.oauth.provider.OauthRefreshTokenService;
import co.jinear.core.service.oauth.provider.OauthScopeService;
import co.jinear.core.service.oauth.provider.OauthTokenIssueService;
import co.jinear.core.validator.oauth.OauthEnabledValidator;
import co.jinear.core.validator.oauth.OauthTokenRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthTokenManager {

    private static final String GRANT_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_REFRESH_TOKEN = "refresh_token";

    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthRefreshTokenService oauthRefreshTokenService;
    private final OauthConnectionService oauthConnectionService;
    private final OauthClientService oauthClientService;
    private final OauthTokenIssueService oauthTokenIssueService;
    private final OauthScopeService oauthScopeService;
    private final OauthTokenRequestValidator oauthTokenRequestValidator;
    private final OauthEnabledValidator oauthEnabledValidator;
    private final OauthClientMetadataVoConverter oauthClientMetadataVoConverter;

    public OauthTokenResponse token(OauthTokenRequest oauthTokenRequest) {
        oauthEnabledValidator.validateOauthIsEnabled();
        String grantType = oauthTokenRequest.getGrantType();
        if (GRANT_AUTHORIZATION_CODE.equals(grantType)) {
            return exchangeAuthorizationCode(oauthTokenRequest);
        }
        if (GRANT_REFRESH_TOKEN.equals(grantType)) {
            return refresh(oauthTokenRequest);
        }
        throw new BusinessException("oauth.error.invalid-grant");
    }

    public OauthClientRegistrationResponse register(OauthClientRegistrationRequest oauthClientRegistrationRequest) {
        oauthEnabledValidator.validateOauthIsEnabled();
        OauthClientMetadataVo registered = oauthClientService.registerDynamicClient(
                oauthClientMetadataVoConverter.map(oauthClientRegistrationRequest));

        OauthClientRegistrationResponse response = new OauthClientRegistrationResponse();
        response.setClientId(registered.getClientId());
        response.setClientIdIssuedAt(System.currentTimeMillis() / 1000);
        response.setClientName(registered.getClientName());
        response.setRedirectUris(registered.getRedirectUris());
        response.setGrantTypes(registered.getGrantTypes());
        response.setResponseTypes(List.of("code"));
        response.setTokenEndpointAuthMethod("none");
        return response;
    }

    public void revoke(OauthRevokeRequest oauthRevokeRequest) {
        oauthEnabledValidator.validateOauthIsEnabled();
        oauthRefreshTokenService.revokeConnectionByToken(oauthRevokeRequest.getToken());
    }

    private OauthTokenResponse exchangeAuthorizationCode(OauthTokenRequest oauthTokenRequest) {
        OauthAuthorizationCodeDto code = oauthAuthorizationCodeService.redeem(oauthTokenRequest.getCode());
        oauthTokenRequestValidator.validateAuthorizationCodeGrant(oauthTokenRequest, code);

        OauthConnectionDto connection = oauthConnectionService.retrieveOptional(code.getOauthConnectionId())
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));

        Set<String> scopes = oauthScopeService.parse(code.getScope());
        return oauthTokenIssueService.issue(connection, scopes);
    }

    private OauthTokenResponse refresh(OauthTokenRequest oauthTokenRequest) {
        OauthRefreshTokenDto refreshToken = oauthRefreshTokenService.redeem(oauthTokenRequest.getRefreshToken());
        OauthConnectionDto connection = oauthConnectionService.retrieveOptional(refreshToken.getOauthConnectionId())
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));
        oauthTokenRequestValidator.validateRefreshTokenGrant(oauthTokenRequest);

        Set<String> effective = oauthScopeService.negotiateRefreshScopes(
                connection.getGrantedScopes(), oauthTokenRequest.getScope());
        String rotated = oauthRefreshTokenService.rotate(refreshToken.getOauthRefreshTokenId());
        return oauthTokenIssueService.issue(connection, effective, rotated);
    }

}
