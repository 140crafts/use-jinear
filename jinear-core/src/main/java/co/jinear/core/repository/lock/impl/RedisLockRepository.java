package co.jinear.core.repository.lock.impl;

import co.jinear.core.repository.lock.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class RedisLockRepository implements LockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Integer increment(String key, Integer ttl, TimeUnit timeUnit) {
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        redisAtomicInteger.expire(ttl, timeUnit);
        return redisAtomicInteger.incrementAndGet();
    }

    @Override
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
