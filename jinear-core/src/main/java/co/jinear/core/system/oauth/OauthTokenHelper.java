package co.jinear.core.system.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthTokenHelper {

    public static final String CLAIM_SCOPE = "scope";
    public static final String CLAIM_CLIENT_ID = "client_id";
    public static final String CLAIM_CONNECTION_ID = "oauth_connection_id";

    private final OauthProperties oauthProperties;
    private final McpProperties mcpProperties;

    @Value("${jwt.oauth.secret}")
    private String secret;

    public String generateAccessToken(String accountId, String connectionId, String clientId, Set<String> scopes, Date expiresAt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_SCOPE, String.join(" ", scopes));
        claims.put(CLAIM_CLIENT_ID, clientId);
        claims.put(CLAIM_CONNECTION_ID, connectionId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(oauthProperties.getIssuerUrl())
                .setSubject(accountId)
                .setAudience(mcpProperties.getResourceUrl())
                .setIssuedAt(DateHelper.now())
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Optional<OauthAccessTokenVo> parseAccessToken(String token) {
        if (Objects.isNull(token) || token.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            if (!mcpProperties.getResourceUrl().equals(claims.getAudience())) {
                log.warn("[OAUTH] Rejecting token issued for another audience: {}", claims.getAudience());
                return Optional.empty();
            }
            if (!oauthProperties.getIssuerUrl().equals(claims.getIssuer())) {
                log.warn("[OAUTH] Rejecting token issued by another issuer: {}", claims.getIssuer());
                return Optional.empty();
            }

            OauthAccessTokenVo vo = new OauthAccessTokenVo();
            vo.setAccountId(claims.getSubject());
            vo.setConnectionId(claims.get(CLAIM_CONNECTION_ID, String.class));
            vo.setClientId(claims.get(CLAIM_CLIENT_ID, String.class));
            vo.setScopes(parseScopes(claims.get(CLAIM_SCOPE, String.class)));
            vo.setExpiresAt(claims.getExpiration());
            return Optional.of(vo);
        } catch (Exception exception) {
            log.debug("[OAUTH] Access token rejected: {}", exception.getMessage());
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
