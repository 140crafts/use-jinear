package co.jinear.core.model.dto.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class InMemoryCacheItem {

    private final Object item;
    private final LocalDateTime expiresAt;

    public boolean isExpired() {
        if (Objects.isNull(expiresAt)) {
            return Boolean.FALSE;
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isNotExpired() {
        return !isExpired();
    }
}
