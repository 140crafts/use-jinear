package co.jinear.core.repository.cache.materialfts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockMaterialFtsDirtyStateRepository implements MaterialFtsDirtyStateRepository {

    @Override
    public void markDirty() {
        log.info("[Mock] Mark material fts dirty.");
    }

    @Override
    public boolean isDirty() {
        return Boolean.FALSE;
    }

    @Override
    public void clearDirty() {
        log.info("[Mock] Clear material fts dirty.");
    }
}
