package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.converter.oauth.OauthClientMetadataVoConverter;
import co.jinear.core.model.request.oauth.OauthClientRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.net.UnknownHostException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CimdResolver {

    private static final int MAX_DOCUMENT_BYTES = 64 * 1024;

    private final OauthProperties oauthProperties;
    private final ObjectMapper objectMapper;
    private final OauthClientMetadataVoConverter oauthClientMetadataVoConverter;

    public OauthClientMetadataVo resolve(String clientId) {
        URI uri = validateClientIdUrl(clientId);
        assertHostIsFetchable(uri.getHost());

        OauthClientRegistrationRequest document = readDocument(fetch(uri));

        if (!clientId.equals(document.getClientId())) {
            log.warn("[OAUTH] CIMD document is not self referential. url: {}, declared: {}", clientId, document.getClientId());
            throw new BusinessException("oauth.error.invalid-client");
        }

        List<String> redirectUris = Objects.isNull(document.getRedirectUris())
                ? List.of()
                : document.getRedirectUris();
        if (redirectUris.isEmpty()) {
            throw new BusinessException("oauth.error.invalid-client");
        }
        assertRedirectUrisAreAcceptable(uri, redirectUris);

        OauthClientMetadataVo vo = oauthClientMetadataVoConverter.map(document);
        vo.setClientId(clientId);
        vo.setRedirectUris(redirectUris);
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
        } catch (UnknownHostException exception) {
            throw new BusinessException("oauth.error.invalid-client");
        }
    }

    private boolean isUniqueLocalIpv6(InetAddress address) {
        byte[] bytes = address.getAddress();
        return bytes.length == 16 && (bytes[0] & 0xFE) == 0xFC;
    }

    private String fetch(URI uri) {
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

    private OauthClientRegistrationRequest readDocument(String body) {
        try {
            return objectMapper.readValue(body, OauthClientRegistrationRequest.class);
        } catch (JsonProcessingException exception) {
            log.warn("[OAUTH] CIMD document could not be read: {}", exception.getMessage());
            throw new BusinessException("oauth.error.invalid-client");
        }
    }
}
