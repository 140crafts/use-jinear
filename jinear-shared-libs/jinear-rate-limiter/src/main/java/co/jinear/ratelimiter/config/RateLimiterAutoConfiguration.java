package co.jinear.ratelimiter.config;

import co.jinear.ratelimiter.filter.RateLimitingFilter;
import co.jinear.ratelimiter.resolver.ClientIpResolver;
import co.jinear.ratelimiter.resolver.DefaultClientIpResolver;
import co.jinear.ratelimiter.service.RateLimitService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = "jinear.security.rate-limit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RateLimiterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StatefulRedisConnection<String, byte[]> rateLimitRedisConnection(
            org.springframework.boot.autoconfigure.data.redis.RedisProperties redisProperties) {
        String redisUri = String.format("redis://%s@%s:%d",
                redisProperties.getPassword() != null ? redisProperties.getPassword() : "",
                redisProperties.getHost(),
                redisProperties.getPort());
        RedisClient redisClient = RedisClient.create(redisUri);
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimitService rateLimitService(RateLimitProperties properties,
                                              StatefulRedisConnection<String, byte[]> rateLimitRedisConnection) {
        return new RateLimitService(properties, rateLimitRedisConnection);
    }

    @Bean
    @ConditionalOnMissingBean(ClientIpResolver.class)
    public ClientIpResolver clientIpResolver(RateLimitProperties properties) {
        return new DefaultClientIpResolver(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimitingFilter rateLimitingFilter(RateLimitService rateLimitService, ClientIpResolver clientIpResolver) {
        log.info("Rate limiting filter enabled");
        return new RateLimitingFilter(rateLimitService, clientIpResolver);
    }
}

