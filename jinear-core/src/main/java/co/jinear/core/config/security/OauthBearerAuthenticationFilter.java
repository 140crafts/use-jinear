package co.jinear.core.config.security;

import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.oauth.provider.OauthAccessTokenResolver;
import co.jinear.core.system.mcp.McpPaths;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthBearerAuthenticationFilter extends OncePerRequestFilter {

    private final OauthAccessTokenResolver oauthAccessTokenResolver;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !McpPaths.MCP_ENDPOINT.equals(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            oauthAccessTokenResolver.extractBearer(request.getHeader(AUTHORIZATION))
                    .flatMap(oauthAccessTokenResolver::resolve)
                    .ifPresent(this::setAuthentication);
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(OauthAccessTokenVo vo) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                vo.getAccountId(),
                null,
                List.of(new SimpleGrantedAuthority(RoleType.USER.getAuthority())));
        authentication.setDetails(vo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static Optional<OauthAccessTokenVo> currentAccessToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> authentication.getDetails())
                .filter(OauthAccessTokenVo.class::isInstance)
                .map(OauthAccessTokenVo.class::cast);
    }
}
