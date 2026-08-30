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

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class McpProperties {

    @Value("${jinear.mcp.enabled:false}")
    private Boolean enabled = Boolean.FALSE;

    /** OAuth issuer. Must be the origin that serves /.well-known/oauth-authorization-server. */
    @Value("${jinear.mcp.issuer-url}")
    private String issuerUrl;

    /**
     * Canonical MCP resource URI. This is both the RFC 8707 audience and the value a
     * user types into their client, and RFC 9728 requires the two to match exactly,
     * so it is configured rather than derived.
     */
    @Value("${jinear.mcp.resource-url}")
    private String resourceUrl;

    /** Public documentation URL, advertised in protected resource metadata. */
    @Value("${jinear.mcp.documentation-url:https://jinear.co/mcp/}")
    private String documentationUrl;

    @Value("${jinear.mcp.access-token-validity-minutes:60}")
    private Integer accessTokenValidityMinutes = 60;

    @Value("${jinear.mcp.refresh-token-validity-days:30}")
    private Integer refreshTokenValidityDays = 30;

    @Value("${jinear.mcp.authorization-code-validity-seconds:60}")
    private Integer authorizationCodeValiditySeconds = 60;

    @Value("${jinear.mcp.authorization-request-validity-minutes:10}")
    private Integer authorizationRequestValidityMinutes = 10;

    @Value("${jinear.mcp.log-retention-days:30}")
    private Integer logRetentionDays = 30;

    /** RFC 7591 dynamic client registration. Kept for clients that cannot do CIMD. */
    @Value("${jinear.mcp.dcr-enabled:true}")
    private Boolean dcrEnabled = Boolean.TRUE;

    /**
     * Comma separated allowlist of hosts permitted to serve a Client ID Metadata
     * Document. Empty means any public https host is accepted, which is the open
     * policy the spec describes. Private and link local addresses are refused either
     * way by the SSRF guard.
     */
    @Value("${jinear.mcp.cimd-allowed-hosts:}")
    private String cimdAllowedHosts;

    @Value("${jinear.mcp.cimd-fetch-timeout-millis:4000}")
    private Integer cimdFetchTimeoutMillis = 4000;

    /** Maximum items any list tool will return in a single call. */
    @Value("${jinear.mcp.max-page-size:50}")
    private Integer maxPageSize = 50;

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

    public String protectedResourceMetadataUrl() {
        return issuerUrl + "/.well-known/oauth-protected-resource/mcp";
    }
}
