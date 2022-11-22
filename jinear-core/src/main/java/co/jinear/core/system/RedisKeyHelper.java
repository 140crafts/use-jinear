package co.jinear.core.system;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class RedisKeyHelper {

    private static final String KEY_SEPARATOR = ":";
    private static final String LOCK_PREFIX = "lock";
    private static final String COMMON_PREFIX = "bet";

    public static String generateLockKey(LockSourceType source, String value) {
        return generateKey(source.getKey(), LOCK_PREFIX, value);
    }

    public static String generateKey(String... value) {
        StringBuilder builder = new StringBuilder(COMMON_PREFIX);
        Arrays.asList(value).forEach(str -> builder.append(KEY_SEPARATOR).append(str));
        return builder.toString();
    }

    public static String generateKeyWithoutPrefix(String... value) {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(value).forEach(str -> builder.append(KEY_SEPARATOR).append(str));
        return builder.toString();
    }
}
