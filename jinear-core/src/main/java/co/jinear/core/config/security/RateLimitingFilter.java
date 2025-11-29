package co.jinear.core.config.security;

import co.jinear.core.config.properties.RateLimitProperties;
import co.jinear.core.model.enumtype.ratelimit.RateLimitPlan;
import co.jinear.core.service.ratelimit.RateLimitService;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitProperties rateLimitProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Bucket bucket;
        String rateLimitKey;

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            rateLimitKey = authentication.getName();
            bucket = rateLimitService.resolveBucket(rateLimitKey, RateLimitPlan.AUTHENTICATED);
            log.info("Rate limiting by user: {}", rateLimitKey);
        } else {
            rateLimitKey = getClientIp(request);
            bucket = rateLimitService.resolveBucket(rateLimitKey, RateLimitPlan.PUBLIC);
            log.info("Rate limiting by IP: {}", rateLimitKey);
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

    private String getClientIp(HttpServletRequest request) {
        String customHeaderName = rateLimitProperties.getClientIpHeader();
        if (customHeaderName != null && !customHeaderName.isEmpty()) {
            String ipFromHeader = request.getHeader(customHeaderName);
            if (ipFromHeader != null && !ipFromHeader.isEmpty()) {
                log.info("RateLimit customHeaderName: {}", ipFromHeader);
                return ipFromHeader;
            }
        }
        String remoteAddr = request.getRemoteAddr();
        log.info("RateLimit remoteAddr: {}", remoteAddr);
        return remoteAddr;
    }
}
