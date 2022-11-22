package co.jinear.core.service.lock;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.repository.lock.LockRepository;
import co.jinear.core.system.NumberCompareHelper;
import co.jinear.core.system.RedisKeyHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private static final int SUCCESS = 1;
    private final LockRepository lockRepository;

    public void lock(String key, LockSourceType lockSourceType) {
        lock(key, lockSourceType, lockSourceType.getTtl());
    }

    public void unlock(String key, LockSourceType lockSourceType) {
        Optional.ofNullable(key)
                .map(k -> RedisKeyHelper.generateLockKey(lockSourceType, k))
                .ifPresent(lockRepository::remove);
    }

    public boolean isLocked(String key, LockSourceType lockSourceType) {
        return Optional.ofNullable(key)
                .map(k -> RedisKeyHelper.generateLockKey(lockSourceType, k))
                .map(lockRepository::exist)
                .orElse(Boolean.FALSE);
    }

    private void lock(String key, LockSourceType lockSourceType, Integer ttl) {
        Optional.ofNullable(key)
                .map(k -> RedisKeyHelper.generateLockKey(lockSourceType, k))
                .map(k -> lockRepository.increment(k, ttl, lockSourceType.getTimeUnit()))
                .filter(val -> NumberCompareHelper.isEquals(val, SUCCESS))
                .orElseThrow(() -> {
                    throw new LockedException(lockSourceType, key);
                });
    }
}
