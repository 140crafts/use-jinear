package co.jinear.core.repository.calendar;

import co.jinear.core.model.entity.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, String> {

    Optional<Calendar> findByCalendarIdAndPassiveIdIsNull(String calendarId);

    Long countAllByWorkspaceIdAndInitializedByAndIntegrationInfoIdAndPassiveIdIsNull(String workspaceId, String accountId, String integrationInfoId);
}
