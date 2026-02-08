package co.jinear.ratelimiter.resolver;

import co.jinear.ratelimiter.config.RateLimitProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Default implementation of ClientIpResolver.
 * Uses configured header or falls back to remote address.
 */
@RequiredArgsConstructor
public class DefaultClientIpResolver implements ClientIpResolver {

    private final RateLimitProperties properties;

    @Override
    public String resolveClientIp(HttpServletRequest request) {
        String clientIpHeader = properties.getClientIpHeader();

        if (StringUtils.hasText(clientIpHeader)) {
            String headerValue = request.getHeader(clientIpHeader);
            if (StringUtils.hasText(headerValue)) {
                // Handle comma-separated IPs (e.g., X-Forwarded-For: client, proxy1, proxy2)
                String[] ips = headerValue.split(",");
                return ips[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}

