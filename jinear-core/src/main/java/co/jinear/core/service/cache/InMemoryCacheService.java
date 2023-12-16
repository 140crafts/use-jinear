package co.jinear.core.service.cache;

import co.jinear.core.model.dto.cache.InMemoryCacheItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryCacheService {

    private final Map<String, InMemoryCacheItem> cache = new ConcurrentHashMap<>();

    public boolean hasKey(String key) {
        return Optional.of(key)
                .map(cache::get)
                .map(InMemoryCacheItem::isExpired)
                .map(isExpired -> !isExpired)
                .orElse(Boolean.FALSE);
    }

    public Object get(String key) {
        return Optional.of(key)
                .map(cache::get)
                .filter(InMemoryCacheItem::isNotExpired)
                .map(InMemoryCacheItem::getItem)
                .orElse(null);
    }

    public void put(String key, Object item, long ttl) {
        InMemoryCacheItem inMemoryCacheItem = new InMemoryCacheItem(item, LocalDateTime.now().plusSeconds(ttl));
        cache.put(key, inMemoryCacheItem);
        refresh();
    }

    private void refresh() {
        cache.keySet().forEach(key -> {
            InMemoryCacheItem inMemoryCacheItem = cache.get(key);
            if (inMemoryCacheItem.isExpired()) {
                cache.remove(key);
            }
        });
    }
}
