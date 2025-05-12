package co.jinear.core.exception.lock;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class LockedException extends BusinessException {

    private final transient LockSourceType lockSourceType;
    private final String key;

    public LockedException(LockSourceType lockSourceType, String key) {
        super("common.error.system.locked-before");
        this.lockSourceType = lockSourceType;
        this.key = key;
        log.error("Process has been locked before. key: {}, lockSourceType: {}", key, lockSourceType);
    }
}
