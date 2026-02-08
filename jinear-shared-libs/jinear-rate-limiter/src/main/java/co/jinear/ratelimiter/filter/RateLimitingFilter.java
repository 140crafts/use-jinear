package co.jinear.ratelimiter.filter;

import co.jinear.ratelimiter.model.RateLimitPlan;
import co.jinear.ratelimiter.resolver.ClientIpResolver;
import co.jinear.ratelimiter.service.RateLimitService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final ClientIpResolver clientIpResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Bucket bucket;
        String rateLimitKey;

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            rateLimitKey = authentication.getName();
            bucket = rateLimitService.resolveBucket(rateLimitKey, RateLimitPlan.AUTHENTICATED);
        } else {
            rateLimitKey = clientIpResolver.resolveClientIp(request);
            bucket = rateLimitService.resolveBucket(rateLimitKey, RateLimitPlan.PUBLIC);
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            log.warn("Rate limit exceeded for key: {}. Retry after {} seconds", rateLimitKey, waitForRefill);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.addHeader("Retry-After", String.valueOf(waitForRefill));
            response.getWriter().flush();
        }
    }
}

