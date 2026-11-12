package co.jinear.core.repository.lock.impl;

import co.jinear.core.repository.lock.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class RedisLockRepository implements LockRepository {

    private static final String LOCKED = "1";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Boolean acquire(String key, Integer ttl, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, LOCKED, ttl, timeUnit);
    }

    @Override
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
