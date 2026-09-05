package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Settings of the MCP resource server. The authorization server in front of it is
 * configured separately in {@link OauthProperties}.
 * <p>
 * {@code enabled} is the switch for both. MCP is the only resource, so an authorization
 * server running without it would have nothing to issue tokens for.
 */
@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class McpProperties {

    @Value("${jinear.mcp.enabled:false}")
    private Boolean enabled = Boolean.FALSE;

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

    @Value("${jinear.mcp.log-retention-days:30}")
    private Integer logRetentionDays = 30;

    /** Maximum items any list tool will return in a single call. */
    @Value("${jinear.mcp.max-page-size:50}")
    private Integer maxPageSize = 50;
}
