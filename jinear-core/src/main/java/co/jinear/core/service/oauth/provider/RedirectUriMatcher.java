package co.jinear.core.service.oauth.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Component
public class RedirectUriMatcher {

    private static final List<String> LOOPBACK_HOSTS = List.of("127.0.0.1", "::1", "[::1]", "localhost");

    public boolean matchesAny(List<String> registered, String requested) {
        if (Objects.isNull(requested) || requested.isBlank()) {
            return false;
        }
        return registered.stream().anyMatch(candidate -> matches(candidate, requested));
    }

    public boolean matches(String registered, String requested) {
        if (Objects.equals(registered, requested)) {
            return true;
        }
        URI registeredUri = parse(registered);
        URI requestedUri = parse(requested);
        if (Objects.isNull(registeredUri) || Objects.isNull(requestedUri)) {
            return false;
        }
        if (!isLoopback(registeredUri) || !isLoopback(requestedUri)) {
            return false;
        }
        return sameScheme(registeredUri, requestedUri)
                && sameLoopbackHost(registeredUri, requestedUri)
                && Objects.equals(path(registeredUri), path(requestedUri));
    }

    public boolean isLoopback(String uri) {
        URI parsed = parse(uri);
        return Objects.nonNull(parsed) && isLoopback(parsed);
    }

    public boolean allLoopback(List<String> registered) {
        return !registered.isEmpty() && registered.stream().allMatch(this::isLoopback);
    }

    private boolean isLoopback(URI uri) {
        String host = uri.getHost();
        if (Objects.isNull(host)) {
            return false;
        }
        return LOOPBACK_HOSTS.contains(host.toLowerCase(Locale.ROOT));
    }

    private boolean sameScheme(URI first, URI second) {
        return Objects.nonNull(first.getScheme()) && first.getScheme().equalsIgnoreCase(second.getScheme());
    }

    private boolean sameLoopbackHost(URI first, URI second) {
        return Objects.nonNull(first.getHost()) && first.getHost().equalsIgnoreCase(second.getHost());
    }

    private String path(URI uri) {
        String path = uri.getPath();
        return Objects.isNull(path) || path.isEmpty() ? "/" : path;
    }

    private URI parse(String value) {
        try {
            return Objects.isNull(value) ? null : URI.create(value);
        } catch (IllegalArgumentException exception) {
            log.debug("[OAUTH] Unparseable redirect uri: {}", value);
            return null;
        }
    }
}
