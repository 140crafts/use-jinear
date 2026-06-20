package co.jinear.core.repository.cache.taskfts;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
public class RedisTaskFtsDirtyStateRepository implements TaskFtsDirtyStateRepository {

    private static final String DIRTY_KEY = "task-fts:dirty";
    private static final String DIRTY_VALUE = "1";
    private static final Duration TTL = Duration.ofHours(1);

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void markDirty() {
        redisTemplate.opsForValue().set(DIRTY_KEY, DIRTY_VALUE, TTL);
    }

    @Override
    public boolean isDirty() {
        return Boolean.TRUE.equals(redisTemplate.hasKey(DIRTY_KEY));
    }

    @Override
    public void clearDirty() {
        redisTemplate.delete(DIRTY_KEY);
    }
}
