package co.jinear.ratelimiter.service;

import co.jinear.ratelimiter.config.RateLimitProperties;
import co.jinear.ratelimiter.model.RateLimitPlan;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.function.Supplier;

import static co.jinear.ratelimiter.config.RateLimitProperties.RefillType.GREEDY;

@Slf4j
public class RateLimitService {

    private final ProxyManager<String> proxyManager;
    private final RateLimitProperties rateLimitProperties;
    private final StatefulRedisConnection<String, byte[]> redisConnection;

    public RateLimitService(RateLimitProperties rateLimitProperties, StatefulRedisConnection<String, byte[]> redisConnection) {
        this.rateLimitProperties = rateLimitProperties;
        this.redisConnection = redisConnection;
        this.proxyManager = LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofHours(1)))
                .build();
        log.info("Rate limiting service initialized with Redis backend");
    }

    @PreDestroy
    public void destroy() {
        if (redisConnection != null && redisConnection.isOpen()) {
            log.info("Closing rate limit Redis connection");
            redisConnection.close();
        }
    }

    public Bucket resolveBucket(String key, RateLimitPlan plan) {
        Supplier<BucketConfiguration> configurationSupplier = () -> createConfiguration(plan);
        String redisKey = "rate-limit:" + plan.name().toLowerCase() + ":" + key;
        return proxyManager.builder().build(redisKey, configurationSupplier);
    }

    private BucketConfiguration createConfiguration(RateLimitPlan plan) {
        String planName = plan.name().toLowerCase();
        RateLimitProperties.Plan planConfig = rateLimitProperties.getPlans().get(planName);
        if (planConfig == null) {
            throw new IllegalStateException("Rate limit configuration not found for plan: " + planName);
        }
        log.debug("Creating rate limit configuration for plan {}: {} requests per {} minute(s), refill type: {}",
                planName, planConfig.getCapacity(), planConfig.getDurationInMinutes(), planConfig.getRefillType());

        BandwidthBuilder.BandwidthBuilderRefillStage bandwidthBuilderRefillStage = Bandwidth.builder().capacity(planConfig.getCapacity());
        Bandwidth limit;
        if (GREEDY.equals(planConfig.getRefillType())) {
            limit = bandwidthBuilderRefillStage.refillGreedy(planConfig.getCapacity(), Duration.ofMinutes(planConfig.getDurationInMinutes())).build();
        } else {
            limit = bandwidthBuilderRefillStage.refillIntervally(planConfig.getCapacity(), Duration.ofMinutes(planConfig.getDurationInMinutes())).build();
        }
        return BucketConfiguration.builder().addLimit(limit).build();
    }
}

