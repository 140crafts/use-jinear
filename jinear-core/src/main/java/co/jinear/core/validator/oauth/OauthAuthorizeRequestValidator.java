package co.jinear.core.validator.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.model.vo.oauth.OauthErrorVo;
import co.jinear.core.service.oauth.provider.OauthClientService;
import co.jinear.core.service.oauth.provider.PkceValidator;
import co.jinear.core.service.oauth.provider.RedirectUriMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthAuthorizeRequestValidator {

    private static final String RESPONSE_TYPE_CODE = "code";

    private final OauthClientService oauthClientService;
    private final RedirectUriMatcher redirectUriMatcher;
    private final PkceValidator pkceValidator;
    private final McpProperties mcpProperties;

    public void validateClientAndRedirectUri(OauthAuthorizeRequestVo vo) {
        OauthClientMetadataVo client = oauthClientService.resolveForAuthorization(vo.getClientId());
        List<String> registeredRedirects = oauthClientService.redirectUrisOf(client);
        if (!redirectUriMatcher.matchesAny(registeredRedirects, vo.getRedirectUri())) {
            log.warn("[OAUTH] Redirect uri is not registered. clientId: {}, redirectUri: {}", vo.getClientId(), vo.getRedirectUri());
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }
    }

    public Optional<OauthErrorVo> validateRequestParameters(OauthAuthorizeRequestVo vo) {
        if (!RESPONSE_TYPE_CODE.equals(vo.getResponseType())) {
            return Optional.of(new OauthErrorVo("unsupported_response_type", "Only the authorization code flow is supported."));
        }
        if (Objects.isNull(vo.getCodeChallenge()) || vo.getCodeChallenge().isBlank()) {
            return Optional.of(new OauthErrorVo("invalid_request", "A PKCE code challenge is required."));
        }
        if (!pkceValidator.isSupportedMethod(vo.getCodeChallengeMethod())) {
            return Optional.of(new OauthErrorVo("invalid_request", "Only the S256 code challenge method is supported."));
        }
        if (!isResourceAcceptable(vo.getResource())) {
            return Optional.of(new OauthErrorVo("invalid_target", "The requested resource does not match this server."));
        }
        return Optional.empty();
    }

    private boolean isResourceAcceptable(String resource) {
        if (Objects.isNull(resource) || resource.isBlank()) {
            return true;
        }
        return normalize(resource).equals(normalize(mcpProperties.getResourceUrl()));
    }

    private String normalize(String uri) {
        String trimmed = uri.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }
}
