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

/**
 * Establishes the caller identity for a request carrying an OAuth bearer token.
 * <p>
 * {@link #shouldNotFilter} restricts that to the MCP endpoint, which is the only
 * protected resource this server has. Widening the predicate is seam 2 of the three
 * listed on {@link co.jinear.core.manager.oauth.provider.OauthAuthorizationManager}.
 * <p>
 * This filter only authenticates. It never refuses a request, because whether a given
 * JSON-RPC message needs credentials depends on the method inside the body, and reading
 * the body here would consume it before the controller could parse it. The controller
 * owns the refusal and emits the 401 or 403 challenge itself.
 * <p>
 * Running ahead of the rate limiting filter matters: with an authentication in place,
 * buckets are keyed per account instead of per gateway IP, so one busy agent cannot
 * exhaust the shared public allowance.
 */
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

    /**
     * The principal is the account id, which is what every manager reads through
     * SessionInfoService. The credential slot is left empty on purpose: it is where a
     * browser session keeps its parseable JWT, and an MCP token is not one. The token
     * details ride along as the authentication details instead.
     */
    private void setAuthentication(OauthAccessTokenVo vo) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                vo.getAccountId(),
                null,
                List.of(new SimpleGrantedAuthority(RoleType.USER.getAuthority())));
        authentication.setDetails(vo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /** Exposed so the controller can read the identity this filter established. */
    public static Optional<OauthAccessTokenVo> currentAccessToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> authentication.getDetails())
                .filter(OauthAccessTokenVo.class::isInstance)
                .map(OauthAccessTokenVo.class::cast);
    }
}
