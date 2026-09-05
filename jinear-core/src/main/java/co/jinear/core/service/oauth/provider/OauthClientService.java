package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthClient;
import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.repository.oauth.OauthClientRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthClientService {

    private static final String NEW_LINE = "\n";

    private final OauthClientRepository oauthClientRepository;
    private final CimdResolver cimdResolver;
    private final OauthProperties oauthProperties;
    private final PassiveService passiveService;

    public OauthClientMetadataVo resolveForAuthorization(String clientId) {
        if (cimdResolver.looksLikeCimdClientId(clientId)) {
            OauthClientMetadataVo metadata = cimdResolver.resolve(clientId);
            upsertCimdClient(metadata);
            return metadata;
        }
        OauthClient stored = oauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId)
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-client"));
        return toMetadata(stored);
    }

    public Optional<OauthClient> retrieveOptional(String clientId) {
        return oauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId);
    }

    public String displayNameFor(String clientId) {
        if (cimdResolver.looksLikeCimdClientId(clientId)) {
            return hostOf(clientId);
        }
        return retrieveOptional(clientId)
                .map(OauthClient::getClientName)
                .filter(name -> Objects.nonNull(name) && !name.isBlank())
                .orElse(clientId);
    }

    public String hostOf(String uri) {
        try {
            return URI.create(uri).getHost();
        } catch (IllegalArgumentException exception) {
            return uri;
        }
    }

    public OauthClientMetadataVo registerDynamicClient(OauthClientMetadataVo request) {
        if (!Boolean.TRUE.equals(oauthProperties.getDcrEnabled())) {
            throw new BusinessException("oauth.error.registration-disabled");
        }
        if (Objects.isNull(request.getRedirectUris()) || request.getRedirectUris().isEmpty()) {
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }
        request.getRedirectUris().forEach(this::assertRedirectUriIsUsable);

        OauthClient client = new OauthClient();
        client.setClientId(co.jinear.core.system.RandomHelper.generateULID());
        client.setClientName(request.getClientName());
        client.setClientUri(request.getClientUri());
        client.setLogoUri(request.getLogoUri());
        client.setPolicyUri(request.getPolicyUri());
        client.setTosUri(request.getTosUri());
        client.setRedirectUris(String.join(NEW_LINE, request.getRedirectUris()));
        client.setGrantTypes(String.join(" ", defaultGrantTypes(request.getGrantTypes())));
        client.setTokenEndpointAuthMethod("none");
        client.setRegistrationType(OauthClientRegistrationType.DCR);
        client.setSoftwareId(request.getSoftwareId());
        client.setSoftwareVersion(request.getSoftwareVersion());
        client.setClientIdIssuedAt(DateHelper.now());

        OauthClient saved = oauthClientRepository.save(client);
        log.info("[OAUTH] Registered dynamic client. clientId: {}, name: {}", saved.getClientId(), saved.getClientName());
        return toMetadata(saved);
    }

    public Page<OauthClient> listClients(Pageable pageable) {
        return oauthClientRepository.findAllByPassiveIdIsNullOrderByCreatedDateDesc(pageable);
    }

    public void revokeClient(String clientId) {
        OauthClient client = oauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId)
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-client"));
        client.setPassiveId(passiveService.createUserActionPassive());
        oauthClientRepository.save(client);
        log.info("[OAUTH] Revoked client registration. clientId: {}", clientId);
    }

    public List<String> redirectUrisOf(OauthClientMetadataVo metadata) {
        return Objects.isNull(metadata.getRedirectUris()) ? List.of() : metadata.getRedirectUris();
    }

    private void assertRedirectUriIsUsable(String redirectUri) {
        URI parsed;
        try {
            parsed = URI.create(redirectUri);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }
        boolean https = "https".equalsIgnoreCase(parsed.getScheme());
        boolean loopback = Objects.nonNull(parsed.getHost())
                && List.of("127.0.0.1", "::1", "localhost").contains(parsed.getHost().toLowerCase(java.util.Locale.ROOT));
        if (!https && !loopback) {
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }
        if (Objects.nonNull(parsed.getFragment())) {
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }
    }

    private List<String> defaultGrantTypes(List<String> requested) {
        if (Objects.isNull(requested) || requested.isEmpty()) {
            return List.of("authorization_code", "refresh_token");
        }
        return requested;
    }

    private void upsertCimdClient(OauthClientMetadataVo metadata) {
        OauthClient client = oauthClientRepository.findByClientIdAndPassiveIdIsNull(metadata.getClientId())
                .orElseGet(OauthClient::new);
        client.setClientId(metadata.getClientId());
        client.setClientName(metadata.getClientName());
        client.setClientUri(metadata.getClientUri());
        client.setLogoUri(metadata.getLogoUri());
        client.setPolicyUri(metadata.getPolicyUri());
        client.setTosUri(metadata.getTosUri());
        client.setRedirectUris(String.join(NEW_LINE, metadata.getRedirectUris()));
        client.setGrantTypes(String.join(" ", defaultGrantTypes(metadata.getGrantTypes())));
        client.setTokenEndpointAuthMethod("none");
        client.setRegistrationType(OauthClientRegistrationType.CIMD);
        if (Objects.isNull(client.getClientIdIssuedAt())) {
            client.setClientIdIssuedAt(DateHelper.now());
        }
        oauthClientRepository.save(client);
    }

    private OauthClientMetadataVo toMetadata(OauthClient client) {
        OauthClientMetadataVo vo = new OauthClientMetadataVo();
        vo.setClientId(client.getClientId());
        vo.setClientName(client.getClientName());
        vo.setClientUri(client.getClientUri());
        vo.setLogoUri(client.getLogoUri());
        vo.setPolicyUri(client.getPolicyUri());
        vo.setTosUri(client.getTosUri());
        vo.setRedirectUris(splitLines(client.getRedirectUris()));
        vo.setGrantTypes(splitSpaces(client.getGrantTypes()));
        vo.setTokenEndpointAuthMethod(client.getTokenEndpointAuthMethod());
        vo.setSoftwareId(client.getSoftwareId());
        vo.setSoftwareVersion(client.getSoftwareVersion());
        return vo;
    }

    private List<String> splitLines(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(NEW_LINE)).map(String::trim).filter(item -> !item.isEmpty()).toList();
    }

    private List<String> splitSpaces(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.trim().split("\\s+")).toList();
    }
}
