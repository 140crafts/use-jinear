package co.jinear.ratelimiter.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "jinear.security.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private String clientIpHeader;
    private Map<String, Plan> plans;

    @Getter
    @Setter
    public static class Plan {
        private long capacity;
        private long durationInMinutes;
        private RefillType refillType;
    }

    public enum RefillType {
        GREEDY,
        INTERVALLY
    }
}

