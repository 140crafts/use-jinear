package co.jinear.core.service.mcp.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpOauthClient;
import co.jinear.core.model.enumtype.mcp.McpClientRegistrationType;
import co.jinear.core.model.vo.mcp.McpClientMetadataVo;
import co.jinear.core.repository.mcp.McpOauthClientRepository;
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

/**
 * Resolution and registration of OAuth clients.
 * <p>
 * Three registration paths are supported: a client_id that is an https URL is
 * resolved as a Client ID Metadata Document, anything else is looked up as a
 * previously registered dynamic or static client.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpOauthClientService {

    private static final String NEW_LINE = "\n";

    private final McpOauthClientRepository mcpOauthClientRepository;
    private final McpCimdResolver mcpCimdResolver;
    private final McpProperties mcpProperties;
    private final PassiveService passiveService;

    /**
     * Returns the client's effective metadata for an authorization request.
     * <p>
     * A CIMD document is re-fetched on every authorization rather than trusting the
     * stored copy, so a client that changes its redirect URIs cannot be authorized
     * against a stale registration.
     */
    public McpClientMetadataVo resolveForAuthorization(String clientId) {
        if (mcpCimdResolver.looksLikeCimdClientId(clientId)) {
            McpClientMetadataVo metadata = mcpCimdResolver.resolve(clientId);
            upsertCimdClient(metadata);
            return metadata;
        }
        McpOauthClient stored = mcpOauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId)
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-client"));
        return toMetadata(stored);
    }

    public Optional<McpOauthClient> retrieveOptional(String clientId) {
        return mcpOauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId);
    }

    public String displayNameFor(String clientId) {
        if (mcpCimdResolver.looksLikeCimdClientId(clientId)) {
            return hostOf(clientId);
        }
        return retrieveOptional(clientId)
                .map(McpOauthClient::getClientName)
                .filter(name -> Objects.nonNull(name) && !name.isBlank())
                .orElse(clientId);
    }

    /**
     * The consent screen must name the client by the host of its client_id URL rather
     * than by the self asserted client_name, because a metadata document is written by
     * whoever hosts it.
     */
    public String hostOf(String uri) {
        try {
            return URI.create(uri).getHost();
        } catch (IllegalArgumentException exception) {
            return uri;
        }
    }

    public McpClientMetadataVo registerDynamicClient(McpClientMetadataVo request) {
        if (!Boolean.TRUE.equals(mcpProperties.getDcrEnabled())) {
            throw new BusinessException("mcp.error.oauth.registration-disabled");
        }
        if (Objects.isNull(request.getRedirectUris()) || request.getRedirectUris().isEmpty()) {
            throw new BusinessException("mcp.error.oauth.invalid-redirect-uri");
        }
        request.getRedirectUris().forEach(this::assertRedirectUriIsUsable);

        McpOauthClient client = new McpOauthClient();
        client.setClientId(co.jinear.core.system.RandomHelper.generateULID());
        client.setClientName(request.getClientName());
        client.setClientUri(request.getClientUri());
        client.setLogoUri(request.getLogoUri());
        client.setPolicyUri(request.getPolicyUri());
        client.setTosUri(request.getTosUri());
        client.setRedirectUris(String.join(NEW_LINE, request.getRedirectUris()));
        client.setGrantTypes(String.join(" ", defaultGrantTypes(request.getGrantTypes())));
        client.setTokenEndpointAuthMethod("none");
        client.setRegistrationType(McpClientRegistrationType.DCR);
        client.setSoftwareId(request.getSoftwareId());
        client.setSoftwareVersion(request.getSoftwareVersion());
        client.setClientIdIssuedAt(DateHelper.now());

        McpOauthClient saved = mcpOauthClientRepository.save(client);
        log.info("[MCP] Registered dynamic client. clientId: {}, name: {}", saved.getClientId(), saved.getClientName());
        return toMetadata(saved);
    }

    public Page<McpOauthClient> listClients(Pageable pageable) {
        return mcpOauthClientRepository.findAllByPassiveIdIsNullOrderByCreatedDateDesc(pageable);
    }

    public void revokeClient(String clientId) {
        McpOauthClient client = mcpOauthClientRepository.findByClientIdAndPassiveIdIsNull(clientId)
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-client"));
        client.setPassiveId(passiveService.createUserActionPassive());
        mcpOauthClientRepository.save(client);
        log.info("[MCP] Revoked client registration. clientId: {}", clientId);
    }

    public List<String> redirectUrisOf(McpClientMetadataVo metadata) {
        return Objects.isNull(metadata.getRedirectUris()) ? List.of() : metadata.getRedirectUris();
    }

    private void assertRedirectUriIsUsable(String redirectUri) {
        URI parsed;
        try {
            parsed = URI.create(redirectUri);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("mcp.error.oauth.invalid-redirect-uri");
        }
        boolean https = "https".equalsIgnoreCase(parsed.getScheme());
        boolean loopback = Objects.nonNull(parsed.getHost())
                && List.of("127.0.0.1", "::1", "localhost").contains(parsed.getHost().toLowerCase(java.util.Locale.ROOT));
        if (!https && !loopback) {
            throw new BusinessException("mcp.error.oauth.invalid-redirect-uri");
        }
        if (Objects.nonNull(parsed.getFragment())) {
            throw new BusinessException("mcp.error.oauth.invalid-redirect-uri");
        }
    }

    private List<String> defaultGrantTypes(List<String> requested) {
        if (Objects.isNull(requested) || requested.isEmpty()) {
            return List.of("authorization_code", "refresh_token");
        }
        return requested;
    }

    /**
     * Keeps a shadow row for a CIMD client so the management screens and the tool call
     * log have a name to show. The row is never the source of truth for authorization.
     */
    private void upsertCimdClient(McpClientMetadataVo metadata) {
        McpOauthClient client = mcpOauthClientRepository.findByClientIdAndPassiveIdIsNull(metadata.getClientId())
                .orElseGet(McpOauthClient::new);
        client.setClientId(metadata.getClientId());
        client.setClientName(metadata.getClientName());
        client.setClientUri(metadata.getClientUri());
        client.setLogoUri(metadata.getLogoUri());
        client.setPolicyUri(metadata.getPolicyUri());
        client.setTosUri(metadata.getTosUri());
        client.setRedirectUris(String.join(NEW_LINE, metadata.getRedirectUris()));
        client.setGrantTypes(String.join(" ", defaultGrantTypes(metadata.getGrantTypes())));
        client.setTokenEndpointAuthMethod("none");
        client.setRegistrationType(McpClientRegistrationType.CIMD);
        if (Objects.isNull(client.getClientIdIssuedAt())) {
            client.setClientIdIssuedAt(DateHelper.now());
        }
        mcpOauthClientRepository.save(client);
    }

    private McpClientMetadataVo toMetadata(McpOauthClient client) {
        McpClientMetadataVo vo = new McpClientMetadataVo();
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
