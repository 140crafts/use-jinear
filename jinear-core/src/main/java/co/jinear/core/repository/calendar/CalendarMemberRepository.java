package co.jinear.core.repository.calendar;

import co.jinear.core.model.entity.calendar.CalendarMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CalendarMemberRepository extends JpaRepository<CalendarMember, String> {

    Optional<CalendarMember> findFirstByAccountIdAndCalendarIdAndPassiveIdIsNull(String accountId, String calendarId);

    List<CalendarMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);

    Page<CalendarMember> findAllByCalendarIdAndPassiveIdIsNull(String calendarId, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update CalendarMember calendarMember
                set calendarMember.passiveId = :passiveId
                    where
                        calendarMember.calendarId = :calendarId and
                        calendarMember.passiveId is null
                """)
    void removeAllMembers(@Param("calendarId") String calendarId, @Param("passiveId") String passiveId);
}
