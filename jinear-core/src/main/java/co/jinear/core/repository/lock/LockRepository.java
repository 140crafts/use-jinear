package co.jinear.core.repository.lock;

import java.util.concurrent.TimeUnit;

public interface LockRepository {

    Boolean acquire(String key, Integer ttl, TimeUnit timeUnit);

    Boolean exist(String key);

    void remove(String key);
}