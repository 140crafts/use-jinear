package co.jinear.core.oauth;

import co.jinear.core.service.oauth.provider.RedirectUriMatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RedirectUriMatcherTest {

    private final RedirectUriMatcher matcher = new RedirectUriMatcher();

    @Test
    void matchesAnExactHttpsRedirect() {
        assertThat(matcher.matches("https://claude.ai/api/mcp/auth_callback",
                "https://claude.ai/api/mcp/auth_callback")).isTrue();
    }

    @Test
    void rejectsADifferentHttpsPath() {
        assertThat(matcher.matches("https://claude.ai/api/mcp/auth_callback",
                "https://claude.ai/api/mcp/other")).isFalse();
    }

    @Test
    void rejectsADifferentHttpsHost() {
        assertThat(matcher.matches("https://claude.ai/api/mcp/auth_callback",
                "https://evil.example/api/mcp/auth_callback")).isFalse();
    }

    @Test
    void ignoresThePortOnLoopbackIpLiterals() {
        assertThat(matcher.matches("http://127.0.0.1/callback", "http://127.0.0.1:51763/callback")).isTrue();
    }

    @Test
    void ignoresThePortOnLocalhostToo() {
        assertThat(matcher.matches("http://localhost/callback", "http://localhost:3118/callback")).isTrue();
    }

    @Test
    void doesNotTreatLocalhostAndTheIpLiteralAsTheSameHost() {
        assertThat(matcher.matches("http://localhost/callback", "http://127.0.0.1:3118/callback")).isFalse();
    }

    @Test
    void stillComparesTheLoopbackPath() {
        assertThat(matcher.matches("http://localhost/callback", "http://localhost:3118/steal")).isFalse();
    }

    @Test
    void doesNotIgnoreThePortOnAPublicHost() {
        assertThat(matcher.matches("https://example.com/cb", "https://example.com:8443/cb")).isFalse();
    }

    @Test
    void matchesAnyOfTheRegisteredRedirects() {
        List<String> registered = List.of("https://claude.ai/api/mcp/auth_callback", "http://localhost/callback");
        assertThat(matcher.matchesAny(registered, "http://localhost:9999/callback")).isTrue();
        assertThat(matcher.matchesAny(registered, "https://evil.example/cb")).isFalse();
        assertThat(matcher.matchesAny(registered, null)).isFalse();
    }

    @Test
    void flagsAClientThatOnlyEverRedirectsToLoopback() {
        assertThat(matcher.allLoopback(List.of("http://localhost/callback", "http://127.0.0.1/callback"))).isTrue();
        assertThat(matcher.allLoopback(List.of("http://localhost/callback", "https://claude.ai/cb"))).isFalse();
        assertThat(matcher.allLoopback(List.of())).isFalse();
    }
}
