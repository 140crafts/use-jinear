package co.jinear.core.repository.calendar;

import co.jinear.core.model.entity.calendar.CalendarShareKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarShareKeyRepository extends JpaRepository<CalendarShareKey, String> {

    Optional<CalendarShareKey> findByShareableKeyAndPassiveIdIsNull(String shareableKey);

    Optional<CalendarShareKey> findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);
}
