package co.jinear.core.converter.oauth;

import co.jinear.core.model.dto.oauth.OauthClientDto;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.model.dto.oauth.OauthConsentInfoDto;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.model.entity.oauth.OauthClient;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.oauth.provider.OauthScopeService;
import co.jinear.core.service.oauth.provider.RedirectUriMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OauthDtoConverter {

    private final RedirectUriMatcher redirectUriMatcher;
    private final OauthScopeService oauthScopeService;

    public OauthConsentInfoDto convert(OauthAuthorizationRequest request,
                                       OauthClientMetadataVo client,
                                       List<String> registeredRedirects) {
        OauthConsentInfoDto dto = new OauthConsentInfoDto();
        dto.setRequestId(request.getOauthAuthorizationRequestId());
        dto.setClientDisplayHost(hostOf(request.getClientId()));
        dto.setClientName(client.getClientName());
        dto.setClientUri(client.getClientUri());
        dto.setLogoUri(client.getLogoUri());
        dto.setPolicyUri(client.getPolicyUri());
        dto.setTosUri(client.getTosUri());
        dto.setRedirectHost(hostOfRedirect(request.getRedirectUri()));
        dto.setLoopbackOnly(redirectUriMatcher.allLoopback(registeredRedirects));
        dto.setRequestedScopes(List.copyOf(oauthScopeService.parse(request.getScope())));
        return dto;
    }

    public OauthConnectionDto convert(OauthConnection entity, Long callCountLast30Days) {
        OauthConnectionDto dto = new OauthConnectionDto();
        dto.setOauthConnectionId(entity.getOauthConnectionId());
        dto.setAccountId(entity.getAccountId());
        dto.setClientId(entity.getClientId());
        dto.setClientName(entity.getClientName());
        dto.setClientDisplayHost(hostOf(entity.getClientId()));
        dto.setGrantedScopes(splitScopes(entity.getGrantedScopes()));
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastUsedAt(entity.getLastUsedAt());
        dto.setCallCountLast30Days(callCountLast30Days);
        return dto;
    }

    public OauthClientDto convert(OauthClient entity) {
        OauthClientDto dto = new OauthClientDto();
        dto.setClientId(entity.getClientId());
        dto.setClientName(entity.getClientName());
        dto.setClientUri(entity.getClientUri());
        dto.setLogoUri(entity.getLogoUri());
        dto.setRedirectUris(splitLines(entity.getRedirectUris()));
        dto.setRegistrationType(entity.getRegistrationType());
        dto.setClientIdIssuedAt(entity.getClientIdIssuedAt());
        return dto;
    }

    private String hostOf(String clientId) {
        if (Objects.isNull(clientId) || !clientId.startsWith("https://")) {
            return clientId;
        }
        try {
            String host = URI.create(clientId).getHost();
            return Objects.isNull(host) ? clientId : host;
        } catch (IllegalArgumentException exception) {
            return clientId;
        }
    }

    private String hostOfRedirect(String redirectUri) {
        try {
            String host = URI.create(redirectUri).getHost();
            return Objects.isNull(host) ? redirectUri : host;
        } catch (IllegalArgumentException exception) {
            return redirectUri;
        }
    }

    private List<String> splitScopes(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.trim().split("\\s+")).toList();
    }

    private List<String> splitLines(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\n")).map(String::trim).filter(item -> !item.isEmpty()).toList();
    }
}
