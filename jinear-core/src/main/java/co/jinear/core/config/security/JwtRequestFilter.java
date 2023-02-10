package co.jinear.core.config.security;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.system.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Optional;

import static co.jinear.core.system.JwtHelper.JWT_COOKIE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = Optional.of(request)
                    .map(HttpServletRequest::getCookies)
                    .map(Arrays::stream)
                    .map(cookieStream -> cookieStream.filter(cookie -> JWT_COOKIE.equals(cookie.getName()))
                            .findFirst()
                            .map(Cookie::getValue)
                            .orElse(null))
                    .orElse(null);
            Optional.ofNullable(token)
                    .map(this::getAccountId)
                    .filter(accId -> jwtHelper.validateToken(token, accId))
                    .ifPresent(accountId -> setAuthentication(token, accountId));
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token, String accountId) {
        List<SimpleGrantedAuthority> grantedAuthorities = jwtHelper.getGrantedAuthorities(token);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountId, token, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String getAccountId(String jwtToken) {
        try {
            return jwtHelper.getAccountIdFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error("[JWT] Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("[JWT] JWT Token has expired");
            throw new BusinessException("common.error.session-expired");
        }
        return null;
    }
}
