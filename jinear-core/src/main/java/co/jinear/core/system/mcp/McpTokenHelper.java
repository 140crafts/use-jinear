package co.jinear.core.system.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.model.vo.mcp.McpAccessTokenVo;
import co.jinear.core.system.util.DateHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Mints and validates MCP access tokens.
 * <p>
 * These are deliberately signed with a different secret from the session JWT that
 * {@code JwtHelper} issues. An MCP bearer must never authenticate a browser session,
 * and a session cookie must never authenticate a tool call, so the two key spaces are
 * kept apart rather than relying on a claim to tell them apart.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class McpTokenHelper {

    public static final String CLAIM_SCOPE = "scope";
    public static final String CLAIM_CLIENT_ID = "client_id";
    public static final String CLAIM_CONNECTION_ID = "mcp_connection_id";

    private final McpProperties mcpProperties;

    @Value("${jwt.mcp.secret}")
    private String secret;

    public String generateAccessToken(String accountId, String connectionId, String clientId, Set<String> scopes, Date expiresAt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_SCOPE, String.join(" ", scopes));
        claims.put(CLAIM_CLIENT_ID, clientId);
        claims.put(CLAIM_CONNECTION_ID, connectionId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(mcpProperties.getIssuerUrl())
                .setSubject(accountId)
                .setAudience(mcpProperties.getResourceUrl())
                .setIssuedAt(DateHelper.now())
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * Returns the token's claims, or empty when the token is unparseable, expired,
     * signed with another key, or was not issued for this resource server.
     * <p>
     * The audience check is the one the MCP specification calls out as mandatory:
     * without it a token minted for a different resource would be accepted here.
     */
    public Optional<McpAccessTokenVo> parseAccessToken(String token) {
        if (Objects.isNull(token) || token.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            if (!mcpProperties.getResourceUrl().equals(claims.getAudience())) {
                log.warn("[MCP] Rejecting token issued for another audience: {}", claims.getAudience());
                return Optional.empty();
            }
            if (!mcpProperties.getIssuerUrl().equals(claims.getIssuer())) {
                log.warn("[MCP] Rejecting token issued by another issuer: {}", claims.getIssuer());
                return Optional.empty();
            }

            McpAccessTokenVo vo = new McpAccessTokenVo();
            vo.setAccountId(claims.getSubject());
            vo.setConnectionId(claims.get(CLAIM_CONNECTION_ID, String.class));
            vo.setClientId(claims.get(CLAIM_CLIENT_ID, String.class));
            vo.setScopes(parseScopes(claims.get(CLAIM_SCOPE, String.class)));
            vo.setExpiresAt(claims.getExpiration());
            return Optional.of(vo);
        } catch (Exception exception) {
            log.debug("[MCP] Access token rejected: {}", exception.getMessage());
            return Optional.empty();
        }
    }

    private Set<String> parseScopes(String scope) {
        if (Objects.isNull(scope) || scope.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(scope.trim().split("\\s+"))
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }
}
