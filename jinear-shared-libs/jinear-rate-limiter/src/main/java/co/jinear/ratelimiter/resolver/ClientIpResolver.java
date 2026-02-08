package co.jinear.ratelimiter.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for resolving client IP address from HTTP request.
 * Implementations should handle proxy headers like X-Forwarded-For if needed.
 */
public interface ClientIpResolver {

    /**
     * Resolves the client IP address from the HTTP request.
     *
     * @param request the HTTP request
     * @return the client IP address
     */
    String resolveClientIp(HttpServletRequest request);
}

