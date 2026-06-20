package co.jinear.core.repository.cache.taskfts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockTaskFtsDirtyStateRepository implements TaskFtsDirtyStateRepository {

    @Override
    public void markDirty() {
        log.info("[Mock] Mark task fts dirty.");
    }

    @Override
    public boolean isDirty() {
        return Boolean.FALSE;
    }

    @Override
    public void clearDirty() {
        log.info("[Mock] Clear task fts dirty.");
    }
}
