package co.jinear.core.system;

import co.jinear.core.config.properties.RateLimitProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpResolver {

    private final RateLimitProperties rateLimitProperties;

    public String getClientIp(HttpServletRequest request) {
        String customHeaderName = rateLimitProperties.getClientIpHeader();
        if (customHeaderName != null && !customHeaderName.isEmpty()) {
            String ipFromHeader = request.getHeader(customHeaderName);
            if (ipFromHeader != null && !ipFromHeader.isEmpty()) {
                log.debug("RateLimit customHeaderName: {}", ipFromHeader);
                return ipFromHeader;
            }
        }
        String remoteAddr = request.getRemoteAddr();
        log.debug("RateLimit remoteAddr: {}", remoteAddr);
        return remoteAddr;
    }
}
