package co.jinear.core.repository.lock.impl;

import co.jinear.core.repository.lock.LockRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockRedisLockRepository implements LockRepository {

    @Override
    public Integer increment(String key, Integer ttl, TimeUnit timeUnit) {
        return 1;
    }

    @Override
    public Boolean exist(String key) {
        return Boolean.FALSE;
    }

    @Override
    public void remove(String token) {}
}
