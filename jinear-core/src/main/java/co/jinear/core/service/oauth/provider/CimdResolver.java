package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Resolves an https client_id into the client's own OAuth metadata, per the OAuth
 * Client ID Metadata Document draft.
 * <p>
 * CIMD is the registration path the MCP specification prefers and the one Claude
 * picks when our authorization server metadata advertises it, because it avoids a
 * fresh dynamic registration row for every connection.
 * <p>
 * The document is self asserted, so this class only establishes that the document
 * lives where the client_id says it does. Everything a user is shown about the
 * client is derived from the client_id host, never from client_name.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CimdResolver {

    private static final int MAX_DOCUMENT_BYTES = 64 * 1024;

    private final OauthProperties oauthProperties;
    private final ObjectMapper objectMapper;

    public OauthClientMetadataVo resolve(String clientId) {
        URI uri = validateClientIdUrl(clientId);
        assertHostIsFetchable(uri.getHost());

        String body = fetch(uri);
        JsonNode document = readJson(body);

        String declaredClientId = text(document, "client_id");
        if (!clientId.equals(declaredClientId)) {
            log.warn("[OAUTH] CIMD document is not self referential. url: {}, declared: {}", clientId, declaredClientId);
            throw new BusinessException("oauth.error.invalid-client");
        }

        List<String> redirectUris = textList(document, "redirect_uris");
        if (redirectUris.isEmpty()) {
            throw new BusinessException("oauth.error.invalid-client");
        }
        assertRedirectUrisAreAcceptable(uri, redirectUris);

        OauthClientMetadataVo vo = new OauthClientMetadataVo();
        vo.setClientId(clientId);
        vo.setClientName(text(document, "client_name"));
        vo.setClientUri(text(document, "client_uri"));
        vo.setLogoUri(text(document, "logo_uri"));
        vo.setPolicyUri(text(document, "policy_uri"));
        vo.setTosUri(text(document, "tos_uri"));
        vo.setRedirectUris(redirectUris);
        vo.setGrantTypes(textList(document, "grant_types"));
        vo.setTokenEndpointAuthMethod(text(document, "token_endpoint_auth_method"));
        vo.setSoftwareId(text(document, "software_id"));
        vo.setSoftwareVersion(text(document, "software_version"));
        return vo;
    }

    public boolean looksLikeCimdClientId(String clientId) {
        if (Objects.isNull(clientId)) {
            return false;
        }
        return clientId.startsWith("https://");
    }

    private URI validateClientIdUrl(String clientId) {
        URI uri;
        try {
            uri = URI.create(clientId);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("oauth.error.invalid-client");
        }
        boolean valid = "https".equalsIgnoreCase(uri.getScheme())
                && Objects.nonNull(uri.getHost())
                && Objects.nonNull(uri.getPath())
                && !uri.getPath().isEmpty()
                && !"/".equals(uri.getPath())
                && Objects.isNull(uri.getFragment());
        if (!valid) {
            log.warn("[OAUTH] Rejecting client_id that is not a valid metadata document url: {}", clientId);
            throw new BusinessException("oauth.error.invalid-client");
        }
        return uri;
    }

    /**
     * Non loopback redirects must be same origin with the metadata document. Without
     * this an attacker could point a legitimate looking client_id at their own
     * callback. Loopback redirects are exempt because a native client cannot be
     * same origin with an https document.
     */
    private void assertRedirectUrisAreAcceptable(URI documentUri, List<String> redirectUris) {
        for (String redirectUri : redirectUris) {
            URI parsed;
            try {
                parsed = URI.create(redirectUri);
            } catch (IllegalArgumentException exception) {
                throw new BusinessException("oauth.error.invalid-client");
            }
            String host = parsed.getHost();
            boolean loopback = Objects.nonNull(host)
                    && List.of("127.0.0.1", "::1", "localhost").contains(host.toLowerCase(Locale.ROOT));
            if (loopback) {
                continue;
            }
            boolean sameOrigin = "https".equalsIgnoreCase(parsed.getScheme())
                    && documentUri.getHost().equalsIgnoreCase(host)
                    && documentUri.getPort() == parsed.getPort();
            if (!sameOrigin) {
                log.warn("[OAUTH] CIMD redirect uri is neither loopback nor same origin. document: {}, redirect: {}",
                        documentUri, redirectUri);
                throw new BusinessException("oauth.error.invalid-client");
            }
        }
    }

    /**
     * The authorization server takes a URL from an unauthenticated caller and fetches
     * it, so an unguarded implementation is a server side request forgery primitive
     * against anything the backend can reach.
     */
    private void assertHostIsFetchable(String host) {
        List<String> allowedHosts = oauthProperties.cimdAllowedHostList();
        if (!allowedHosts.isEmpty() && !allowedHosts.contains(host.toLowerCase(Locale.ROOT))) {
            log.warn("[OAUTH] CIMD host is not in the configured allowlist: {}", host);
            throw new BusinessException("oauth.error.invalid-client");
        }
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                boolean unreachable = address.isLoopbackAddress()
                        || address.isSiteLocalAddress()
                        || address.isLinkLocalAddress()
                        || address.isAnyLocalAddress()
                        || address.isMulticastAddress()
                        || isUniqueLocalIpv6(address);
                if (unreachable) {
                    log.warn("[OAUTH] Refusing to fetch a CIMD document from a private address. host: {}", host);
                    throw new BusinessException("oauth.error.invalid-client");
                }
            }
        } catch (java.net.UnknownHostException exception) {
            throw new BusinessException("oauth.error.invalid-client");
        }
    }

    private boolean isUniqueLocalIpv6(InetAddress address) {
        byte[] bytes = address.getAddress();
        return bytes.length == 16 && (bytes[0] & 0xFE) == 0xFC;
    }

    private String fetch(URI uri) {
        // HttpClient is only AutoCloseable from Java 21 and this module compiles at 17,
        // so the client is built per call and left to the garbage collector.
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofMillis(oauthProperties.getCimdFetchTimeoutMillis()))
                .build();
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .timeout(Duration.ofMillis(oauthProperties.getCimdFetchTimeoutMillis()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("[OAUTH] CIMD fetch returned {} for {}", response.statusCode(), uri);
                throw new BusinessException("oauth.error.invalid-client");
            }
            String body = response.body();
            if (Objects.isNull(body) || body.length() > MAX_DOCUMENT_BYTES) {
                throw new BusinessException("oauth.error.invalid-client");
            }
            return body;
        } catch (BusinessException businessException) {
            throw businessException;
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new BusinessException("oauth.error.invalid-client");
        } catch (Exception exception) {
            log.warn("[OAUTH] CIMD fetch failed for {}: {}", uri, exception.getMessage());
            throw new BusinessException("oauth.error.invalid-client");
        }
    }

    private JsonNode readJson(String body) {
        try {
            JsonNode node = objectMapper.readTree(body);
            if (Objects.isNull(node) || !node.isObject()) {
                throw new BusinessException("oauth.error.invalid-client");
            }
            return node;
        } catch (BusinessException businessException) {
            throw businessException;
        } catch (Exception exception) {
            throw new BusinessException("oauth.error.invalid-client");
        }
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return Objects.nonNull(value) && value.isTextual() ? value.asText() : null;
    }

    private List<String> textList(JsonNode node, String field) {
        JsonNode value = node.get(field);
        List<String> values = new ArrayList<>();
        if (Objects.nonNull(value) && value.isArray()) {
            value.forEach(item -> {
                if (item.isTextual()) {
                    values.add(item.asText());
                }
            });
        }
        return values;
    }
}
