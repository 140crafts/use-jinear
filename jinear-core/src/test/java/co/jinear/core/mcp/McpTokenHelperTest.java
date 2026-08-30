package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.system.mcp.McpTokenHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Audience validation is the requirement the MCP specification is most explicit about: a
 * server must only accept tokens minted for itself, or a token issued for one resource
 * becomes a key to another.
 */
class McpTokenHelperTest {

    private static final String SECRET = "mcp-secret-used-only-in-tests-0123456789";
    private static final String RESOURCE = "https://api.jinear.test/mcp";
    private static final String ISSUER = "https://api.jinear.test";

    private McpTokenHelper helper;

    @BeforeEach
    void setUp() {
        McpProperties properties = new McpProperties();
        properties.setIssuerUrl(ISSUER);
        properties.setResourceUrl(RESOURCE);
        helper = new McpTokenHelper(properties);
        ReflectionTestUtils.setField(helper, "secret", SECRET);
    }

    @Test
    void roundTripsAnAccessToken() {
        String token = helper.generateAccessToken("account-1", "connection-1", "https://claude.ai/client.json",
                Set.of("tasks:read", "tasks:write"), inOneHour());

        var parsed = helper.parseAccessToken(token);

        assertThat(parsed).isPresent();
        assertThat(parsed.get().getAccountId()).isEqualTo("account-1");
        assertThat(parsed.get().getConnectionId()).isEqualTo("connection-1");
        assertThat(parsed.get().getClientId()).isEqualTo("https://claude.ai/client.json");
        assertThat(parsed.get().getScopes()).containsExactlyInAnyOrder("tasks:read", "tasks:write");
    }

    @Test
    void rejectsATokenMintedForAnotherResource() {
        String foreign = Jwts.builder()
                .setClaims(new HashMap<>(Map.of("scope", "tasks:read")))
                .setIssuer(ISSUER)
                .setSubject("account-1")
                .setAudience("https://someone-else.example/mcp")
                .setExpiration(inOneHour())
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();

        assertThat(helper.parseAccessToken(foreign)).isEmpty();
    }

    @Test
    void rejectsATokenMintedByAnotherIssuer() {
        String foreign = Jwts.builder()
                .setClaims(new HashMap<>(Map.of("scope", "tasks:read")))
                .setIssuer("https://not-us.example")
                .setSubject("account-1")
                .setAudience(RESOURCE)
                .setExpiration(inOneHour())
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();

        assertThat(helper.parseAccessToken(foreign)).isEmpty();
    }

    @Test
    void rejectsATokenSignedWithTheSessionSecret() {
        // The whole point of the separate key: a browser session JWT must not open the
        // MCP endpoint, and an MCP token must not open a browser session.
        String sessionToken = Jwts.builder()
                .setSubject("account-1")
                .setAudience(RESOURCE)
                .setExpiration(inOneHour())
                .signWith(SignatureAlgorithm.HS512, "the-session-secret".getBytes(StandardCharsets.UTF_8))
                .compact();

        assertThat(helper.parseAccessToken(sessionToken)).isEmpty();
    }

    @Test
    void rejectsAnExpiredToken() {
        String expired = helper.generateAccessToken("account-1", "connection-1", "client",
                Set.of("tasks:read"), new Date(System.currentTimeMillis() - 60_000));

        assertThat(helper.parseAccessToken(expired)).isEmpty();
    }

    @Test
    void rejectsGarbage() {
        assertThat(helper.parseAccessToken(null)).isEmpty();
        assertThat(helper.parseAccessToken("")).isEmpty();
        assertThat(helper.parseAccessToken("not-a-jwt")).isEmpty();
    }

    private Date inOneHour() {
        return new Date(System.currentTimeMillis() + 3_600_000);
    }
}
