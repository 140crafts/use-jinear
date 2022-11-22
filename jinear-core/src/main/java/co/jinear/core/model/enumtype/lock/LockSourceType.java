package co.jinear.core.model.enumtype.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum LockSourceType {
    BALANCE("balance", 1, TimeUnit.HOURS);

    private String key;
    private int ttl;
    private TimeUnit timeUnit;
}
