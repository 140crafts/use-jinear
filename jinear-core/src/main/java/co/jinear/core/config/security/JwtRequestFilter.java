package co.jinear.core.config.security;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.service.robot.RobotTokenValidator;
import co.jinear.core.system.mcp.McpPaths;
import co.jinear.core.system.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.system.JwtHelper.JWT_COOKIE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtHelper jwtHelper;
    private final RobotTokenValidator robotTokenValidator;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return McpPaths.MCP_ENDPOINT.equals(path) || path.startsWith(McpPaths.WELL_KNOWN_PREFIX);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            retrieveAndValidateJwtCookie(request);
            retrieveAndValidateBearerToken(request);
        }
        chain.doFilter(request, response);
    }

    private void retrieveAndValidateBearerToken(HttpServletRequest request) {
        String token = retrieveTokenFromBearerToken(request);
        validateAndSetAuthentication(token);
    }

    private void retrieveAndValidateJwtCookie(HttpServletRequest request) {
        String token = retrieveTokenFromCookie(request);
        validateAndSetAuthentication(token);
    }

    private String retrieveTokenFromBearerToken(HttpServletRequest request) {
        return Optional.of(request)
                .map(r -> r.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .orElse(null);
    }

    private String retrieveTokenFromCookie(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .map(Arrays::stream)
                .map(cookieStream -> cookieStream.filter(cookie -> JWT_COOKIE.equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null))
                .orElse(null);
    }

    private void validateAndSetAuthentication(String token) {
        if (Objects.isNull(token)) {
            return;
        }
        try {
            jwtHelper.validateToken(token);
            checkAndValidateRobotToken(token);
            setAuthentication(token);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException exception) {
            log.warn("[JWT] Ignoring an unreadable token: {}", exception.getClass().getSimpleName());
        }
    }

    private void checkAndValidateRobotToken(String token) {
        if (Boolean.TRUE.equals(jwtHelper.isRobot(token))) {
            robotTokenValidator.validate(token);
        }
    }

    private void setAuthentication(String token) {
        String subjectId = getSubjectId(token);
        List<SimpleGrantedAuthority> grantedAuthorities = jwtHelper.getGrantedAuthorities(token);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(subjectId, token, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String getSubjectId(String jwtToken) {
        try {
            return jwtHelper.getSubjectFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error("[JWT] Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("[JWT] JWT Token has expired");
            throw new BusinessException("common.error.session-expired");
        }
        return null;
    }
}
