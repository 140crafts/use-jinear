package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * The authorization server's own settings. Nothing here knows about MCP.
 * <p>
 * There is deliberately no enabled flag. MCP is the only resource this server protects,
 * so {@code jinear.mcp.enabled} is the single switch for both halves. When a second
 * resource arrives, enablement becomes per resource and this class gains nothing.
 */
@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class OauthProperties {

    /** Issuer. Must be the origin that serves /.well-known/oauth-authorization-server. */
    @Value("${jinear.oauth.issuer-url}")
    private String issuerUrl;

    /** Public documentation URL, advertised as service_documentation. */
    @Value("${jinear.oauth.documentation-url:https://jinear.co/mcp/}")
    private String documentationUrl;

    @Value("${jinear.oauth.access-token-validity-minutes:60}")
    private Integer accessTokenValidityMinutes = 60;

    @Value("${jinear.oauth.refresh-token-validity-days:30}")
    private Integer refreshTokenValidityDays = 30;

    @Value("${jinear.oauth.authorization-code-validity-seconds:60}")
    private Integer authorizationCodeValiditySeconds = 60;

    @Value("${jinear.oauth.authorization-request-validity-minutes:10}")
    private Integer authorizationRequestValidityMinutes = 10;

    /** RFC 7591 dynamic client registration. Kept for clients that cannot do CIMD. */
    @Value("${jinear.oauth.dcr-enabled:true}")
    private Boolean dcrEnabled = Boolean.TRUE;

    /**
     * Comma separated allowlist of hosts permitted to serve a Client ID Metadata
     * Document. Empty means any public https host is accepted, which is the open
     * policy the spec describes. Private and link local addresses are refused either
     * way by the SSRF guard.
     */
    @Value("${jinear.oauth.cimd-allowed-hosts:}")
    private String cimdAllowedHosts;

    @Value("${jinear.oauth.cimd-fetch-timeout-millis:4000}")
    private Integer cimdFetchTimeoutMillis = 4000;

    public List<String> cimdAllowedHostList() {
        if (Objects.isNull(cimdAllowedHosts) || cimdAllowedHosts.isBlank()) {
            return List.of();
        }
        return Arrays.stream(cimdAllowedHosts.split(","))
                .map(String::trim)
                .filter(host -> !host.isEmpty())
                .map(host -> host.toLowerCase(Locale.ROOT))
                .toList();
    }
}
