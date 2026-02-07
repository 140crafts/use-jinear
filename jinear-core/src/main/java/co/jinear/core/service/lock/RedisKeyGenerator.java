package co.jinear.core.service.lock;

import co.jinear.core.config.RedisConfiguration;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RedisKeyGenerator {

    private static final String KEY_SEPARATOR = ":";
    private static final String LOCK_PREFIX = "lock";

    private final RedisConfiguration redisConfiguration;

    public String generateLockKey(LockSourceType source, String value) {
        return generateKey(source.getKey(), LOCK_PREFIX, value);
    }

    public String generateKey(String... value) {
        String prefix = redisConfiguration.getKeyPrefix();
        StringBuilder builder = new StringBuilder(prefix);
        Arrays.asList(value).forEach(str -> builder.append(KEY_SEPARATOR).append(str));
        return builder.toString();
    }
}
