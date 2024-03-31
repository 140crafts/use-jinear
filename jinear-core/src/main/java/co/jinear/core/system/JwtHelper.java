package co.jinear.core.system;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.system.util.DateHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtHelper {

    public static final String JWT_COOKIE = "JWT";
    public static final int JWT_TOKEN_VALIDITY = 180;
    public static final String AUTHORITIES = "authorities";
    public static final String SESSION_INFO_ID = "session_info_id";
    public static final String LOCALE = "locale";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.is-secure}")
    private Boolean isSecure;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getAccountIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities(String token) {
        ArrayList<String> auths = getClaimFromToken(token, claims -> claims.get(AUTHORITIES, (new ArrayList<String>()).getClass()));
        return auths.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public LocaleType getLocaleFromToken(String token) {
        String locale = getClaimFromToken(token, claims -> claims.get(LOCALE, String.class));
        return EnumUtils.getEnum(LocaleType.class, locale);
    }

    public String getSessionIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(SESSION_INFO_ID, String.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(AuthResponseVo authResponseVo, String sessionInfoId) {
        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = authResponseVo.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put(AUTHORITIES, authorities);
        claims.put(SESSION_INFO_ID, sessionInfoId);
        claims.put(LOCALE, authResponseVo.getLocale());
        return doGenerateToken(claims, authResponseVo.getAccountId());
    }

    public Boolean validateToken(String token, String accountId) {
        final String accountIdFromToken = getAccountIdFromToken(token);
        return (accountIdFromToken.equals(accountId) && !isTokenExpired(token));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
    }

    public Map<String, Object> getAllClaimsFromTokenUnsigned(String jwt) {
        var claimsBase64 = jwt.substring(jwt.indexOf('.') + 1, jwt.lastIndexOf('.'));
        try {
            byte[] claimsByte = Base64.getDecoder().decode(claimsBase64);
            return MAPPER.readValue(claimsByte, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception exception) {
            return Collections.emptyMap();
        }
    }

    public Boolean isSecure() {
        return isSecure;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(DateHelper.now())
                .setExpiration(DateHelper.addDays(DateHelper.now(), JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}
