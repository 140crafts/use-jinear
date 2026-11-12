package co.jinear.core.service.lock;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyGenerator {

    private static final String KEY_SEPARATOR = ":";
    private static final String LOCK_PREFIX = "lock";

    public String generateLockKey(LockSourceType source, String value) {
        return String.join(KEY_SEPARATOR, source.getKey(), LOCK_PREFIX, value);
    }
}
