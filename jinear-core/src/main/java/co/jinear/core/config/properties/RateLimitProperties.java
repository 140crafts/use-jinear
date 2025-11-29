package co.jinear.core.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "jinear.security.rate-limit")
public class RateLimitProperties {

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